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
package com.siphyc.test.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.NamingException;
import org.eclipse.jetty.plus.jndi.Resource;
import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.LoggerFactory;

public class TestDataSource {
    
    
      public static final org.slf4j.Logger logger = LoggerFactory.getLogger(TestDataSource.class);
    
     public static void setUpDemoH2Resource() {
        try {
            JdbcConnectionPool dataPool = JdbcConnectionPool.create(
                    "jdbc:h2:mem:base;DB_CLOSE_DELAY=-1", "blah","blah-blah");
            new Resource(null, "jdbc/conName", dataPool);
        } catch (NamingException ex) {
            logger.info("Error setting up demo resource \n" + ex);
        }
    }

    public static void initH2DemoInMemoryDatabase() throws SQLException {

        final String DB_DRIVER = "org.h2.Driver";
        final String DB_CONNECTION = "jdbc:h2:mem:base;DB_CLOSE_DELAY=-1";
        final String DB_USER = "blah";
        final String DB_PASSWORD = "blah-blah";

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
    
    
}
