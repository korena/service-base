package com.siphyc.service;

import java.util.Map;
import javax.enterprise.inject.Default;
import javax.ws.rs.core.Response;

/**
 *
 * @author korena
 */
@Default
public class DefaultService implements ServiceInterface {

    private String responseBuild(String method) {
        return "{\"successful\": \"hello from" + method + "\"}";
    }

    @Override
    public Response getPhones(int limit) {
        return Response.ok(responseBuild("default getPhones")).build();
    }

    @Override
    public Response getPhone(int id) {
        return Response.ok(responseBuild("default getPhone")).build();
    }

    /**
     * Never called ... why u hatin' ?
     *
     * @param map
     * @return
     */
    @Override
    public Response addPhone(Map<String, Object> map) {
        return Response.ok(responseBuild("default addPhones")).build();
    }

    /**
     * Never called ... Don't hate !
     *
     * @param map
     * @return
     */
    @Override
    public Response editPhone(Map<String, Object> map) {
        return Response.ok(responseBuild("default editPhones")).build();
    }

    @Override
    public Response addOrEdit(Map<String, Object> map) {
        return Response.ok(responseBuild("default addOrEdit")).build();
    }

    @Override
    public Response deletePhone(String id) {
        return Response.ok(responseBuild("default deletePhones")).build();
    }

}
