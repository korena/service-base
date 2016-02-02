
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.LoggerFactory;

import javax.naming.Reference;
import java.net.URL;
import java.security.ProtectionDomain;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.NamingException;
import org.eclipse.jetty.plus.jndi.Resource;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.SecuredRedirectHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.h2.jdbcx.JdbcConnectionPool;

/**
 *
 * @author korena
 */
public class JServer {

    /**
     * logger
     */
    final static org.slf4j.Logger logger = LoggerFactory.getLogger(JServer.class);

    static Server embed_server;
    private static Properties conf;

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        conf = loadConfig();

        embed_server = new Server();
        if (conf.getProperty("use_ssl").equalsIgnoreCase("True")) {
            embed_server.setConnectors(new Connector[]{setupHttpConnectors(Integer.parseInt(conf.getProperty("http_port")),
                Integer.parseInt(conf.getProperty("https_real_port"))),
                setupHttpsConnectors(Integer.parseInt(conf.getProperty("https_port")),
                Integer.parseInt(conf.getProperty("https_real_port")),
                conf.getProperty("keystorePath"), conf.getProperty("keystorePass"))});
        } else {
            embed_server.setConnectors(new Connector[]{setupHttpConnectors(Integer.parseInt(conf.getProperty("http_port")),
                Integer.parseInt(conf.getProperty("https_real_port")))});
        }
        /*JNDI AND OTHER SERVER SPECIFIC BUSINESS*/
        org.eclipse.jetty.webapp.Configuration.ClassList classlist = org.eclipse.jetty.webapp.Configuration.ClassList.setServerDefault(embed_server);
        // this line sets up the server to pick up JNDI related configurations in override-web.xml. This is a direct copy off the documentation.
        classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration", "org.eclipse.jetty.plus.webapp.EnvConfiguration", "org.eclipse.jetty.plus.webapp.PlusConfiguration");
        classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration");

        
        WebAppContext webapp = setupWebapp();
        // Create server level handler tree (assigning handlers to server ...)
        HandlerList handlers = new HandlerList();
        if (conf.getProperty("use_ssl").equalsIgnoreCase("True")) {
            handlers.addHandler(setupHttpsRedirect(webapp));
        } else {
            handlers.addHandler(webapp);
        }
        /**
         * the default handler deals with requests that aren't handled by any
         * assigned handler, gives 404 mostly.
         */
        handlers.addHandler(new DefaultHandler());
        embed_server.setHandler(handlers);

        logger.debug("The configured DB is " + conf.getProperty("DB"));
        /**
         * Depends on the active.properties file's DB property, which picks it's
         * variables according to the active maven profile, the default is h2.
         */
        switch (conf.getProperty("DB")) {
            case "mysql":
                setUpMySqlResource();
                break;
            case "h2":
                setUpDemoH2Resource();
                initH2DemoInMemoryDatabase();
                break;
        }

        new org.eclipse.jetty.plus.jndi.Resource(webapp,
                "BeanManager",
                new Reference("javax.enterprise.inject.spi.BeanManager", "org.jboss.weld.resources.ManagerObjectFactory", null));

        try {
            embed_server.start();
            embed_server.join();
        } catch (Exception ex) {
            logger.error("Could not bootstrap jetty server: " + ex);
        }
    }

    /**
     * This method is used to setup a secure connector, it requires valid
     * keystore information in the config.properties file. Things to note: The
     * securePort parameter is the port that this server will bind to, the
     * purpose of this port is to allow the application to be started as a
     * normal user without the need to bind to one of the first 1024 port
     * numbers, which are privileged and need the application to be started as
     * root. So this parameter will be an unprivileged port number (high
     * number).
     *
     * The actualSecurePort parameter will follow the standard port 443, so that
     * your services can be reached using the standard ssl port.
     *
     * This implies that there has to be some kind of an external tool
     * (NFTables/iptables/firewall/proxy server ...) that will route requests
     * arriving at actualSecurePort to securePort for this application to
     * handle.
     *
     * Depending on the use_ssl setting found in active.properties file, this
     * method might be used or ignored. If ignored, your application will not
     * need a keystore, and you will be using unencrypted http ... not good for
     * you.
     *
     *
     * @param securePort
     * @param actualSecurePort
     * @param keystorePath
     * @param ketStorePass
     * @return
     */
    private static ServerConnector setupHttpsConnectors(int securePort, int actualSecurePort, String keystorePath, String ketStorePass) {
        /*CONNECTORS BUSINESS*/
        HttpConfiguration httpsConf = new HttpConfiguration();
        httpsConf.setSecurePort(actualSecurePort);
        httpsConf.setSecureScheme("https");
        httpsConf.addCustomizer(new SecureRequestCustomizer());
        //Set up SSL keystore
        SslContextFactory sslContextFactory = new SslContextFactory(keystorePath);
        sslContextFactory.setKeyStorePassword(ketStorePass);
        ServerConnector sslConnector = new ServerConnector(embed_server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(httpsConf));
        sslConnector.setPort(securePort);
        sslConnector.setName("secured");
        return sslConnector;
    }

    /**
     * This method sets up an insecure connector, and binds it to the port
     * defined in active.properties as http_port, if you do not want to use
     * https (why wouldn't you !?) , this is the only connector that will be
     * used to reach your application.
     *
     * @param port
     * @param securePort
     * @return
     */
    private static ServerConnector setupHttpConnectors(int port, int securePort) {
        /*CONNECTORS BUSINESS*/
        HttpConfiguration httpConf = new HttpConfiguration();
        httpConf.setSecurePort(securePort);
        httpConf.setSecureScheme("https");

        ServerConnector httpConnector = new ServerConnector(embed_server,
                new HttpConnectionFactory(httpConf));
        httpConnector.setName("unsecured"); // named connector
        httpConnector.setPort(port);
        return httpConnector;
    }

    /**
     *
     * @param webapp
     * @return
     */
    private static ContextHandlerCollection setupHttpsRedirect(WebAppContext webapp) {
        /*HANDLERS BUSINESS*/
        SecuredRedirectHandler securedRedirect = new SecuredRedirectHandler();

        // Establish all handlers that have a context
        ContextHandlerCollection contextHandlers = new ContextHandlerCollection();
        webapp.setVirtualHosts(new String[]{"@secured"});   // handles requests that come through the sslConnector connector ...

        ContextHandler redirectHandler = new ContextHandler();
        redirectHandler.setContextPath("/");
        redirectHandler.setHandler(securedRedirect);
        redirectHandler.setVirtualHosts(new String[]{"@unsecured"});  // handls requests that come through the Connector (unsecured) ...
        contextHandlers.setHandlers(new Handler[]{redirectHandler, webapp});
        return contextHandlers;
    }

    /**
     * This method is called to setup the application, it prepares the
     * application context, defines your web.xml and override-web.xml etc ...
     * This is basically what makes your application behave as if it was
     * deployed in a full fledged standard container ...
     *
     * @return WebAppContext
     */
    private static WebAppContext setupWebapp() {
        ProtectionDomain domain = JServer.class.getProtectionDomain();
        URL location = domain.getCodeSource().getLocation();
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setLogUrlOnStart(true);
        webapp.setParentLoaderPriority(true);
        webapp.setWar(location.toExternalForm());
        webapp.setDescriptor("WEB-INF/web.xml");
        webapp.setOverrideDescriptor("WEB-INF/override-web.xml");
        webapp.prependServerClass("-org.eclipse.jetty.servlet.,-org.eclipse.jetty.server."); //TODO: too much exposure, check which classes are needed.
        webapp.setServer(embed_server);
        return webapp;
    }

    /**
     * example code to setup Mysql resource for JNDI lookup by JPA
     * implementation (EclipseLink here) You may follow this model for
     * registering Database resources, you also have to expose your resource's
     * related jars to the server classpath in the maven pom file (checkout the
     * execution with id 'jetty-classpath' in this project's pom.xml file).
     */
    private static void setUpMySqlResource() {
        try {
            MysqlConnectionPoolDataSource dataPool = new MysqlConnectionPoolDataSource();
            dataPool.setURL(conf.getProperty("url"));
            dataPool.setUser(conf.getProperty("user"));
            dataPool.setPassword(conf.getProperty("password"));
            if (conf.getProperty("DB_useSSL").equalsIgnoreCase("true")) {
                dataPool.setUseSSL(true);
                dataPool.setRequireSSL(true);
                System.setProperty("javax.net.ssl.trustStore", conf.getProperty("keystorePath"));
                System.setProperty("javax.net.ssl.trustStorePassword", conf.getProperty("keystorePass"));
                // debugging ssl connection ...
//                System.setProperty("javax.net.debug", "all");
            }
            new Resource(null, "jdbc/conName", dataPool);
        } catch (NamingException ex) {
            // unlikely
            logger.info("Error setting up Mysql resource \n" + ex);
        }
    }

    /**
     * This is a demo in-memory H2 database resource setup method, the setup
     * process is almost identical to the above MySql resource registration.
     *
     * A couple of things to note: The in-memory nature of H2 is not relevant to
     * this method, it's defined in the config.properties under
     * {@link src/main/resources/dev/} as: "jdbc:h2:mem:base;DB_CLOSE_DELAY=-1"
     * .. see that 'mem' part ? yup, that's what makes it in-memory. The
     * consequence of this is the fact that we need to initialize the database,
     * otherwise EclipseLink will complain that there are no Tables (android &
     * iphone) in the database (base) it is trying to communicate with. This is
     * only for demo purposes, you should only keep the code that relates to
     * your choice of database. Always remember to remove the maven dependencies
     * for all the junk this demo reference project adds.
     *
     */
    private static void setUpDemoH2Resource() {
        try {
            JdbcConnectionPool dataPool = JdbcConnectionPool.create(
                    conf.getProperty("url"), conf.getProperty("user"), conf.getProperty("password"));
            new Resource(null, "jdbc/conName", dataPool);
        } catch (NamingException ex) {
            logger.info("Error setting up demo resource \n" + ex);
        }
    }

    /**
     * Remove me !! Im Only here for demo purposes, All I'm doing is populate
     * the in-memory H2 database so that this application would work without you
     * having to setup a real database, I am not in anyway important for the
     * server code to work in your application, you could however use me to test
     * your DAO layer in your test units, by moving me to your test jars and
     * setting up properly in your test class's
     *
     * @before/@beforeClass methods ...
     *
     * @throws SQLException
     */
    private static void initH2DemoInMemoryDatabase() throws SQLException {

        final String DB_DRIVER = "org.h2.Driver";
        final String DB_CONNECTION = conf.getProperty("url");
        final String DB_USER = conf.getProperty("user");
        final String DB_PASSWORD = conf.getProperty("password");

        Connection dbConnection;

        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);

            PreparedStatement createPreparedStatement;

            String CreateAndroidQuery = "CREATE TABLE ANDROID(id int primary key auto_increment,"
                    + " customer varchar(255),"
                    + " model varchar(255),"
                    + " status TINYINT,"
                    + " created TIMESTAMP,"
                    + " UPDATED TIMESTAMP)";
            String CreateIphoneQuery = "CREATE TABLE IPHONE(id int primary key auto_increment,"
                    + " customer varchar(255),"
                    + " model varchar(255),"
                    + " status TINYINT,"
                    + " created TIMESTAMP,"
                    + " UPDATED TIMESTAMP)";

            dbConnection.setAutoCommit(false);

            createPreparedStatement = dbConnection.prepareStatement(CreateAndroidQuery);
            createPreparedStatement.executeUpdate();
            createPreparedStatement.close();

            createPreparedStatement = dbConnection.prepareStatement(CreateIphoneQuery);
            createPreparedStatement.executeUpdate();
            createPreparedStatement.close();

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * This method loads the properties from active.properties file in the
     * project resources. Note that this method is accessible to the server, but
     * not the application, the application defines another loadConfig method in
     * {@link com.siphyc.utils.Utilities#loadConfig() loadConfig}
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static Properties loadConfig() throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.load(JServer.class
                .getResourceAsStream("/active.properties"));
        return prop;
    }

    /**
     *
     * @throws Exception
     */
    public static void stopServer() throws Exception {
        if (embed_server != null) {
            embed_server.stop();
            embed_server.join();
            embed_server.destroy();
            embed_server = null;
        }
    }
}
