package com.siphyc.test.app;


import java.net.URI;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.jersey.test.JerseyTest;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 *
 * @author korena
 */

public class CdiEnabled extends JerseyTest {

    Weld weld;

    public CdiEnabled() {
    }

    @Override
    public void setUp() throws Exception {
        weld = new Weld();
        WeldContainer container = weld.initialize();
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        weld.shutdown();
        super.tearDown();
    }

    @Override
    protected Application configure() {
        return new TestApp();
    }

    @Override
    protected URI getBaseUri() {
        return UriBuilder.fromUri(super.getBaseUri()).path("service-base").build();

    }

}
