/* 
 * The MIT License
 *
==================================================================================
 * Copyright 2016 SIPHYC SYSTEMS Sdn Bhd All Rights Reserved.
 *
 * project reference code contributed by Moaz Korena <korena@siphyc.com,moazkorena@gmail.com>
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
package com.siphyc.test.dao;

import com.siphyc.dao.AndroidJpaController;
import com.siphyc.model.Android;
import com.siphyc.test.app.TestDataSource;
import java.sql.SQLException;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.LoggerFactory;

/**
 *
 * @author korena
 */
public class AndroidJpaControllerTest {

    public AndroidJpaControllerTest() {
    }

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(AndroidJpaControllerTest.class);

    private static AndroidJpaController controller;

    @BeforeClass
    public static void setUpClass() {
        
        TestDataSource.setUpDemoH2Resource();
        try {
            TestDataSource.initH2DemoInMemoryDatabase();
        } catch (SQLException ex) {
            logger.error("Exception setting up test datasource: "+ex);
        }
        controller = new AndroidJpaController(); // run with runner if you want to inject ...
    }

    @AfterClass
    public static void tearDownClass() {
        
        // NO.
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void CRUDAndroidTest() throws Exception {

        Android newEntry = new Android(1, "jes", "Android M", true, new Date(), new Date());
            // create and read
            controller.create(newEntry);
            Android retreived = controller.findAndroid(1);
            assertEquals(newEntry, retreived);
            // update and read
            retreived.setCustomer("edited name");
            controller.edit(retreived);
            Android editedRetrieved = controller.findAndroid(1);
            assertEquals(retreived, editedRetrieved);
            // delete and read
            controller.destroy(1);
            Android removed = controller.findAndroid(1);
            assertEquals(removed,null);
    }
}
