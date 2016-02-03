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
package com.siphyc.endpoints;

import com.siphyc.service.ServiceInterface;
import com.siphyc.service.android;
import com.siphyc.service.iphone;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

@RequestScoped
@Path("resource")
public class EndpointExample {

    @Inject
    @android
    private ServiceInterface androidService;
    @Inject
    @iphone
    private ServiceInterface iphoneService;

    @GET
    @Path("/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("param") String parameter,
            @DefaultValue("5") @QueryParam("limit") int limit,
            @QueryParam("similar") String use_similar,
            @QueryParam("query") String query,
            @QueryParam("exclude") String exclude) {
        String output = "{\"ERROR\":\"unsupported request\"}";
        Response defaultResponse = Response.status(Response.Status.NOT_IMPLEMENTED).entity(output).build();
        switch (parameter) {
            case "android": {
                return androidService.getPhones(limit);
            }
            case "iphone": {
                return iphoneService.getPhones(limit);
            }
            default: {
                return defaultResponse;
            }
        }
    }

    @POST
    @Path("/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@PathParam("param") String parameter, FormDataMultiPart form) {
        String output = "{\"ERROR\":\"unsupported request\"}";
        Response defaultResponse = Response.status(Response.Status.NOT_IMPLEMENTED).entity(output).build();
        switch (parameter) {
            case "android": {
                // build a form map ...
                Map<String, Object> formMap = new HashMap<>();
                formMap.putAll(form.getFields());

                return androidService.addOrEdit(formMap);
            }
            case "iphone": {
                // build a form map ...
                Map<String, Object> formMap = new HashMap<>();
                formMap.putAll(form.getFields());

                return iphoneService.addOrEdit(formMap);
            }
            default: {
                return defaultResponse;
            }
        }
    }

    @DELETE
    @Path("/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("param") String parameter,
            @QueryParam("id") String id) {
        String output = "{\"ERROR\":\"unsupported request\"}";
        Response defaultResponse = Response.status(Response.Status.NOT_IMPLEMENTED).entity(output).build();

        switch (parameter) {
            case "android": {
                return androidService.deletePhone(id);
            }
            case "iphone": {
                return iphoneService.deletePhone(id);
            }
            default: {
                return defaultResponse;
            }
        }
    }

}
