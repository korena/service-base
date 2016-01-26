package com.siphyc.mock.service;

import com.siphyc.service.*;
import com.google.gson.Gson;
import com.siphyc.mock.dao.IphoneJpaController;
import com.siphyc.model.Iphone;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.slf4j.LoggerFactory;

/**
 *
 * @author korena
 */
@iphone
public class IphoneService implements ServiceInterface {


    IphoneJpaController controller = new IphoneJpaController();

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(AndroidService.class);

    @Override
    public Response getPhones(int limit) {

        List<Iphone> results = controller.findIphoneEntities(limit, 0);
        if (results.size() > 0) {
            Gson builder = new Gson();
            return Response.ok("[" + builder.toJson(results) + "]").build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

    @Override
    public Response getPhone(int id) {
        Iphone result = controller.findIphone(id);
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

        Iphone newPhone = new Iphone(null, customer, model, false, new Date(), new Date());
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
        Iphone existingPhone = controller.findIphone(Integer.parseInt(id));

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
        if (idTest != null) {
            id = idTest.get(0).getValue();
        }
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
