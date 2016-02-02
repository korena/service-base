package com.siphyc.binder;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author korena
 * @Comment this is done within the application, because jersey is initialized
 * at the web application's level, other containers (like glassfish) would
 * transparently do it at the server level, this is a baseless wild guess.
 */
public class AppJerseyBinder extends ResourceConfig {

    public AppJerseyBinder() {
        super(MultiPartFeature.class);
        String[] pacakgesToScan = {"com.siphyc.endpoints",
            "com.siphyc.service",
            "com.siphyc.dbConnect",
            "com.siphyc.dao","com.siphyc.model"};
        packages(true, pacakgesToScan);
    }
}
