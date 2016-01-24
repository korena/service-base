package com.siphyc.service;

import com.google.gson.Gson;
import com.siphyc.dao.AndroidJpaController;
import com.siphyc.model.Android;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.slf4j.LoggerFactory;

/**
 *
 * @author korena
 */
@android @Default
public class AndroidService implements ServiceInterface {

    @Inject
    AndroidJpaController controller;
    
    @Inject
    public AndroidService(){

    }

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(AndroidService.class);

    @Override
    public Response getPhones(int limit) {

        List<Android> results = controller.findAndroidEntities(limit, 0);
        if (results.size() > 0) {
            Gson builder = new Gson();
            return Response.ok("[" + builder.toJson(results) + "]").build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

    @Override
    public Response getPhone(int id) {
        Android result = controller.findAndroid(id);
        if (result != null) {
            Gson builder = new Gson();
            return Response.ok(builder.toJson(result)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response addPhone(Map<String, Object> form) {
        String customer = ((List<FormDataBodyPart>) form.get("customer")).get(0).getValue();
        String model = ((List<FormDataBodyPart>) form.get("model")).get(0).getValue();

        Android newPhone = new Android(null, customer, model, false, new Date(), new Date());
        try {
            controller.create(newPhone);
            return Response.ok().build();
        } catch (Exception ex) {
            logger.error("failed to persist changes " + ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response editPhone(Map<String, Object> form) {
        String id = ((List<FormDataBodyPart>) form.get("id")).get(0).getValue();
        String customer = ((List<FormDataBodyPart>) form.get("customer")).get(0).getValue();
        String model = ((List<FormDataBodyPart>) form.get("model")).get(0).getValue();
        String status = ((List<FormDataBodyPart>) form.get("status")).get(0).getValue();

        /*Does this phone exist?*/
        Android existingPhone = controller.findAndroid(Integer.parseInt(id));

        if (existingPhone == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        existingPhone.setCustomer(customer);
        existingPhone.setModel(model);
        existingPhone.setStatus(Boolean.parseBoolean(status));
        existingPhone.setUpdated(new Date());

        try {
            controller.edit(existingPhone);
            Gson builder = new Gson();
            return Response.ok(builder.toJson(existingPhone)).build();
        } catch (Exception ex) {
            logger.error("failed to persist changes " + ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response addOrEdit(Map<String, Object> form) {
        List<FormDataBodyPart> idTest = ((List<FormDataBodyPart>) form.get("id"));
        String id = null;
        if(idTest != null)
         id = idTest.get(0).getValue();

        if (id == null) {
            return this.addPhone(form);
        } else {
            return this.editPhone(form);
        }
    }

    @Override
    public Response deletePhone(String id) {
        try {
            controller.destroy(Integer.parseInt(id));
            return Response.ok("{\"status\":\"deleted\"}").build();
        } catch (Exception ex) {
            logger.error("failed to persist changes " + ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
