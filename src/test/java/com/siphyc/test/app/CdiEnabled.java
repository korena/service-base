package com.siphyc.test.app;


import java.net.URI;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.test.JerseyTest;


/**
 *
 * @author korena
 */

public class CdiEnabled extends JerseyTest {


    public CdiEnabled() {
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected Application configure() {
        return new TestApp();
    }

    @Override
    protected void configureClient(ClientConfig config) {
       config.register(MultiPartFeature.class);
    }
    
    @Override
    protected URI getBaseUri() {
        return UriBuilder.fromUri(super.getBaseUri()).path("service-base").build();
    }

}
