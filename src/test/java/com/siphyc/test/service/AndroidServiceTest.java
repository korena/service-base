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
package com.siphyc.test.service;

import com.siphyc.mock.dao.AndroidJpaController;
import com.siphyc.service.AndroidService;
import com.siphyc.service.ServiceInterface;
import com.siphyc.service.android;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author korena
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({AndroidService.class, AndroidJpaController.class})
public class AndroidServiceTest {

    @Inject
    @android
    ServiceInterface androidService;

    public AndroidServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void androidCreateTest() {

        FormDataMultiPart newAndroidFormData = new FormDataMultiPart();
        newAndroidFormData.field("shouldBeCustomer", "new one ma");
        newAndroidFormData.field("model", "Android N");

        Map<String, Object> newAndroid = new HashMap<>();
        newAndroid.putAll(newAndroidFormData.getFields());
        Response badRequestResponse = androidService.addOrEdit(newAndroid);

        /**
         * 400 response code test (null pointer exception)
         */
        assertEquals(badRequestResponse.getStatus(), 400);

        
        // OK I'm not finishing this, you get the idea ...
    }
}
