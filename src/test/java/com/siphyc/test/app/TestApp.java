/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siphyc.test.app;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * JAX-RS application.
 *
 * @author Jonathan Benoit (jonathan.benoit at oracle.com)
 */
@ApplicationPath("/*")
public class TestApp extends ResourceConfig {

    public TestApp() {
        super(MultiPartFeature.class);
        String[] pacakgesToScan = {
            "com.siphyc.endpoints",
            "com.siphyc.mock.service"
        };
        register(new InternalsBinder());
        packages(true, pacakgesToScan);
    }

}
