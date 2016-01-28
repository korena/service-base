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
     * {@link com.siphyc.test.app.CdiEnabled# configureClient(ClientConfig) configureClient}
     * method The server side also added support for MultiPartFeature, checkout:
     * {@link com.siphyc.test.app.TestApp#TestApp() TestApp} Constructor. In
     * both cases, you're looking at register(MultiPartFeature.class)
     */
    @Test
    public void AndroidResourcePOSTTest() {
        FormDataMultiPart newAndroidFormData = new FormDataMultiPart();
        newAndroidFormData.field("customer", "jessy");
        newAndroidFormData.field("model", "Android N");
        Entity<FormDataMultiPart> dataEntity = Entity.entity(newAndroidFormData, MediaType.MULTIPART_FORM_DATA);
        Response postSuccessResponse = target().path("resource/android").request().post(dataEntity, Response.class);

        /**
         * test 200 response code (post)
         */
        assertEquals(postSuccessResponse.getStatus(), 200);

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

        FormDataMultiPart failedUpdateAndroidFormData = new FormDataMultiPart();
        failedUpdateAndroidFormData.field("id", "2");  // Note that the mockup service will not recognize id=2 ...
        failedUpdateAndroidFormData.field("customer", "john");
        failedUpdateAndroidFormData.field("model", "Android O");
        failedUpdateAndroidFormData.field("status", "true");

        Entity<FormDataMultiPart> badDataEntity = Entity.entity(failedUpdateAndroidFormData, MediaType.MULTIPART_FORM_DATA);
        Response updateFailedResponse = target().path("resource/android").request().post(badDataEntity, Response.class);

        /**
         * test 404 response code (update)
         */
        assertEquals(updateFailedResponse.getStatus(), 404);

        /**
         * TODO: other relevant POST tests
         */
    }

    @Test
    public void AndroidResourceDeleteTest() {

    }

}
