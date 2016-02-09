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
package com.siphyc.test.endpoints;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.siphyc.model.Android;
import com.siphyc.test.app.CdiEnabled;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import static org.hamcrest.CoreMatchers.containsString;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.LoggerFactory;

/**
 *
 * @author moaz korena (korena@siphyc.com/moazkorena@gmail.com)
 *
 * This class is intended to test the Endpoints of your web service, meaning you
 * should provide mockups for all needed code stack that does not directly
 * belong to jersey registered resources. This example test class only tests
 * android endpoint ... iphone is ignored ...
 *
 * NOTE: Injection points in the tested resources are handled by HK2, To enabled
 * CDI Injected resources (including qualifier annotated injection points),
 * AbstractBinder was extended (checkout
 * {@link com.siphyc.test.app.InternalsBinder internalsBinder} class)
 *
 */
public class EndpointExampleTest extends CdiEnabled {

    /**
     *
     */
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(EndpointExampleTest.class);

    /**
     * A test method for GET requests (Not very thorough).
     */
    @Test
    public void AndroidResourceGETTest() {
        int limit = 2;
        Response response = target().path("resource/android").queryParam("limit", limit).request().get();

        /**
         * test 200 response code
         */
        assertEquals(response.getStatus(), 200);

        String responseText = response.readEntity(String.class);
        Gson builder = new Gson();
        Type androidArrayListType = new TypeToken<ArrayList<Android>>() {
        }.getType();
        List responseObj = (List<Android>) builder.fromJson(responseText, androidArrayListType);

        /**
         * test limited response objects count
         */
        assertEquals(limit, responseObj.size());

        Response error404Response = target().path("resoorce/android").request().get();

        /**
         * test 404 error
         */
        assertEquals(error404Response.getStatus(), 404);

        Response error501Response = target().path("resource/androids").request().get();

        /**
         * test 501 error
         */
        assertEquals(error501Response.getStatus(), 501);
        assertThat(error501Response.readEntity(String.class), containsString("unsupported request"));

        /**
         * TODO: other relevant GET tests ...
         */
    }

    /**
     * A test method for POST requests (Not very thorough)... NOTE: the default
     * client of jersey test framework had to be configured to handle
     * FormDataMultipart, checkout the overridden
     * {@link com.siphyc.test.app.CdiEnabled#configureClient(ClientConfig) configureClient}
     * method. The server side also added support for MultiPartFeature,
     * checkout: {@link com.siphyc.test.app.TestApp#TestApp() TestApp}
     * Constructor. In both cases, you're looking at
     * register(MultiPartFeature.class);
     */
    @Test
    public void AndroidResourcePOSTTest() {
        
        FormDataMultiPart badNewAndroidFormData = new FormDataMultiPart();
        badNewAndroidFormData.field("shouldBeCustomer", "new customer");
        badNewAndroidFormData.field("model", "Android M");
        Entity<FormDataMultiPart> badRequestDataEntity = Entity.entity(badNewAndroidFormData, MediaType.MULTIPART_FORM_DATA);

        Response postBadResponse = target().path("resource/android").request().post(badRequestDataEntity, Response.class);
        /**
         * test 400 response code (post)
         */
        assertEquals(postBadResponse.getStatus(), 400);
        
        
        FormDataMultiPart newAndroidFormData = new FormDataMultiPart();
        newAndroidFormData.field("customer", "jessy");
        newAndroidFormData.field("model", "Android N");
        Entity<FormDataMultiPart> dataEntity = Entity.entity(newAndroidFormData, MediaType.MULTIPART_FORM_DATA);

        Response postSuccessResponse = target().path("resource/android").request().post(dataEntity, Response.class);
        /**
         * test 201 response code (post)
         */
        assertEquals(postSuccessResponse.getStatus(), 201);

        newAndroidFormData = new FormDataMultiPart();
        newAndroidFormData.field("customer", "jessy");
        newAndroidFormData.field("model", "Iphone");
        Entity<FormDataMultiPart> exceptionWorthyDataEntity = Entity.entity(newAndroidFormData, MediaType.MULTIPART_FORM_DATA);

        Response postExceptionResponse = target().path("resource/android").request().post(exceptionWorthyDataEntity, Response.class);

        /**
         * test 500 response code (post)
         */
        assertEquals(postExceptionResponse.getStatus(), 500);

        FormDataMultiPart updateAndroidFormData = new FormDataMultiPart();
        updateAndroidFormData.field("id", "1");
        updateAndroidFormData.field("customer", "john");
        updateAndroidFormData.field("model", "Android O");
        updateAndroidFormData.field("status", "true");

        Entity<FormDataMultiPart> anotherDataEntity = Entity.entity(updateAndroidFormData, MediaType.MULTIPART_FORM_DATA);
        Response updateSuccessResponse = target().path("resource/android").request().post(anotherDataEntity, Response.class);

        /**
         * test 200 response code (update)
         */
        assertEquals(updateSuccessResponse.getStatus(), 200);

        FormDataMultiPart failedNonExistentUpdateAndroidFormData = new FormDataMultiPart();
        failedNonExistentUpdateAndroidFormData.field("id", "2");  // Note that the mockup service will not recognize id=2 ...
        failedNonExistentUpdateAndroidFormData.field("customer", "john");
        failedNonExistentUpdateAndroidFormData.field("model", "Android O");
        failedNonExistentUpdateAndroidFormData.field("status", "true");

        
        /**
         * Note: NonExistent should never happen because of the check (look at the code) ...
         */
        Entity<FormDataMultiPart> badDataEntity = Entity.entity(failedNonExistentUpdateAndroidFormData, MediaType.MULTIPART_FORM_DATA);
        Response nonExistentUpdateFailedResponse = target().path("resource/android").request().post(badDataEntity, Response.class);

        /**
         * test 404 response code (update)
         */
        assertEquals(nonExistentUpdateFailedResponse.getStatus(), 404);

        
        
        /**
         * Rollback should not happen .. but if it does for some datasource issue ,
         * we can still catch it, but the response would be an ugly 500 ...
         */
        FormDataMultiPart failedRollbackUpdateAndroidFormData = new FormDataMultiPart();
        failedRollbackUpdateAndroidFormData.field("id", "3"); 
        failedRollbackUpdateAndroidFormData.field("customer", "john");
        failedRollbackUpdateAndroidFormData.field("model", "Android O");
        failedRollbackUpdateAndroidFormData.field("status", "true");

        Entity<FormDataMultiPart> badDataEntity2 = Entity.entity(failedRollbackUpdateAndroidFormData, MediaType.MULTIPART_FORM_DATA);
        Response rollbackupdateFailedResponse = target().path("resource/android").request().post(badDataEntity2, Response.class);

        /**
         * test 404 response code (update)
         */
        assertEquals(rollbackupdateFailedResponse.getStatus(), 404);

    }

    @Test
    public void AndroidResourceDeleteTest() {

        Response successfullDelete = target().path("resource/android").queryParam("id", 1).request().delete();
        /**
         * test 200 response code (delete)
         */
        assertEquals(successfullDelete.getStatus(), 200);

         Response badRequestDelete = target().path("resource/android").request().delete();
        /**
         * test 400 response code (delete)
         */
        assertEquals(badRequestDelete.getStatus(), 400);
        
        
        Response failedDelete = target().path("resource/android").queryParam("id", 2).request().delete();
        /**
         * test 404 response code (delete)
         */
        assertEquals(failedDelete.getStatus(), 404);

        Response failedDelete2 = target().path("resource/android").queryParam("id", 3).request().delete();
        /**
         * test 500 response code (delete)
         */
        assertEquals(failedDelete2.getStatus(), 500);

    }

}
