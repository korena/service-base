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

/**
 *
 * @author korena
 */
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
