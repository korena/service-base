/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siphyc.test.app;

import com.siphyc.endpoints.EndpointExample;
import com.siphyc.mock.service.AndroidService;
import com.siphyc.mock.service.MockServiceFactory;
import com.siphyc.service.ServiceInterface;
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
            "com.siphyc.service",
            "com.siphyc.dao",
            "com.siphyc.mock.service",
            "com.siphyc.mock.dao"};
//        register(new InternalsBinder());
//        register(ServiceInterface.class);
//        register(AndroidService.class);
//        register(MockServiceFactory.class);
//        register(EndpointExample.class);
        packages(true, pacakgesToScan);
    }

}
