


import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.LoggerFactory;

import javax.naming.Reference;
import java.net.URL;
import java.security.ProtectionDomain;
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

public class JServer {

    /**
     * logger
     */
    final static org.slf4j.Logger logger = LoggerFactory.getLogger(JServer.class);

    static Server embed_server;
    private static Properties conf;

    public static void main(String[] args) throws Exception {

        conf = loadConfig();

        embed_server = new Server();
        embed_server.setConnectors(new Connector[]{setupHttpConnectors(Integer.parseInt(conf.getProperty("http_port")),
                Integer.parseInt(conf.getProperty("https_real_port"))),
            setupHttpsConnectors(Integer.parseInt(conf.getProperty("https_port")),
                    Integer.parseInt(conf.getProperty("https_real_port")),
                    conf.getProperty("keystorePath"), conf.getProperty("keystorePass"))});

        /*JNDI AND OTHER SERVER SPECIFIC BUSINESS*/
        org.eclipse.jetty.webapp.Configuration.ClassList classlist = org.eclipse.jetty.webapp.Configuration.ClassList.setServerDefault(embed_server);
        // this line sets up the server to pick up JNDI related configurations in override-web.xml. This is a direct copy off the documentation.
        classlist.addAfter("org.eclipse.jetty.webapp.FragmentConfiguration", "org.eclipse.jetty.plus.webapp.EnvConfiguration", "org.eclipse.jetty.plus.webapp.PlusConfiguration");
        classlist.addBefore("org.eclipse.jetty.webapp.JettyWebXmlConfiguration", "org.eclipse.jetty.annotations.AnnotationConfiguration");

        WebAppContext webapp = setupWebapp();
        // Create server level handler tree (assigning handlers to server ...)
        HandlerList handlers = new HandlerList();
        handlers.addHandler(setupHttpsRedirect(webapp));
        handlers.addHandler(new DefaultHandler());
        embed_server.setHandler(handlers);

        /*what would I do with a reference ??*/
        new org.eclipse.jetty.plus.jndi.Resource(webapp, "BeanManager",
                new Reference("javax.enterprise.inject.spi.BeanManager",
                        "org.jboss.weld.resources.ManagerObjectFactory", null));
        
        setUpMySqlResource();
        try{
        embed_server.start();
        embed_server.join();
        }catch(Exception ex){
        logger.error("Could not bootstrap jetty server: "+ex);
        }
    }

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

    private static ContextHandlerCollection setupHttpsRedirect(WebAppContext webapp) {
        /*HANDLERS BUSINESS*/
        SecuredRedirectHandler securedRedirect = new SecuredRedirectHandler();
        // Establish all handlers that have a context
        ContextHandlerCollection contextHandlers = new ContextHandlerCollection();
        webapp.setVirtualHosts(new String[]{"@secured"});   // handles requests that come through the sslConnector connector ...

        ContextHandler redirectHandler = new ContextHandler();
        redirectHandler.setContextPath("/");
        redirectHandler.setHandler(securedRedirect);
        redirectHandler.setVirtualHosts(new String[]{"@unsecured"});  // handls requests that come through the Connector connector ...
        contextHandlers.setHandlers(new Handler[]{redirectHandler, webapp});
        return contextHandlers;
    }

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
        webapp.prependServerClass("-org.eclipse.jetty.server.,-org.eclipse.jetty.server.,org.eclipse.jetty.servlet.ServletContextHandler.Decorator");
        return webapp;
    }

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
            logger.info("Someone registered this resource before, so it's expected to be available ..."+
                    "If that's not the case, deal with your exception: \n"+ex);
        }
    }

    private static Properties loadConfig() throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.load(JServer.class.getResourceAsStream("/active.properties"));
        return prop;
    }

    public static void stopServer() throws Exception {
        if (embed_server != null) {
            embed_server.stop();
            embed_server.join();
            embed_server.destroy();
            embed_server = null;
        }
    }
}
