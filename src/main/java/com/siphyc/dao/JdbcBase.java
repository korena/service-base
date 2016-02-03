/* 
 * The MIT License
 *
==================================================================================
 * Copyright 2016 SIPHYC SYSTEMS Sdn Bhd All Rights Reserved.
 *
 * This reference code is maintained by Moaz Korena <korena@siphyc.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.siphyc.dao;

import com.siphyc.utils.Utilities;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class JdbcBase {

    /**
     * logger
     */
    final static org.slf4j.Logger logger = LoggerFactory.getLogger(JdbcBase.class);

    /**
     * Uses JNDI and Datasource (preferred style).
     *
     * @return
     * @throws java.sql.SQLException
     */
    public Connection getJNDIConnection() throws SQLException, NullPointerException {
        String DATASOURCE_CONTEXT = "java:comp/env/jdbc/Oauth";

        Connection result = null;
        try {
            Context initialContext = new InitialContext();
            if (initialContext == null) {
                logger.debug("JNDI problem. Cannot get InitialContext.");
                throw new NullPointerException();
            }
            DataSource datasource = (DataSource) initialContext.lookup(DATASOURCE_CONTEXT);

            result = (Connection) datasource.getConnection();

        } catch (NullPointerException | NamingException | SQLException ex) {

            if (ex instanceof NullPointerException) {
                logger.error("" + ex);

            } else if (ex instanceof NamingException) {
                logger.error("Cannot get connection .. trying to get simple connection instead ...");
                result = getSimpleConnection();  // this is very bad ... but better than not connecting at all!
            } else if (ex instanceof SQLException) {
                logger.error("The underlaying database system rejected whatever you are trying to do and responded\n"
                        + "with error code:" + ((SQLException) ex).getErrorCode());
            }
        }
        return result;
    }

    /**
     * Uses DriverManager.
     *
     * @return
     */
    public Connection getSimpleConnection() {
        //See your driver documentation for the proper format of this string :
        String DB_CONN_STRING = Utilities.conf.getProperty("url");

        if (Utilities.conf.getProperty("DB_useSSL").equalsIgnoreCase("true")) {
            System.setProperty("javax.net.ssl.trustStore", Utilities.conf.getProperty("keystorePath"));
            System.setProperty("javax.net.ssl.trustStorePassword",Utilities.conf.getProperty("keystorePass"));
            DB_CONN_STRING = DB_CONN_STRING.concat("?useSSL=true&requireSSL=true");
        }
        //Provided by your driver documentation. In this case, a MySql driver is used : 
        String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
        String USER_NAME = Utilities.conf.getProperty("user");
        String PASSWORD = Utilities.conf.getProperty("password");
        Connection result = null;
        try {
            Class.forName(DRIVER_CLASS_NAME).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            if (ex instanceof ClassNotFoundException) {
                logger.error("Check classpath. Cannot load db driver: " + DRIVER_CLASS_NAME);
            } else if (ex instanceof InstantiationException) {
                logger.error("Check classpath. Cannot instantiate db driver: " + DRIVER_CLASS_NAME);
            } else if (ex instanceof IllegalAccessException) {
                logger.error("Your DB user cridentials are rejected:" + ((IllegalAccessException) ex).getCause());
            }
        }

        try {
            result = (Connection) DriverManager.getConnection(DB_CONN_STRING, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            logger.error("The underlaying database system rejected whatever you are trying to do and responded\n"
                    + "with error code:" + ((SQLException) e).getErrorCode());
        }
        return result;
    }

    private static void log(Object aObject) {
        System.out.println(aObject);
    }
}
