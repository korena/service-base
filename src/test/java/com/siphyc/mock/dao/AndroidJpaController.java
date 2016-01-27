/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siphyc.mock.dao;

import com.siphyc.model.Android;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author korena
 */
public class AndroidJpaController {

    @Inject
    public AndroidJpaController() {
    }

    public List<Android> findAndroidEntities(int limit, int i) {
        List<Android> results = new ArrayList<>();
        // dummy data ...
        int end = (limit == 0?5:limit);
        for (int j = 0; j < end; j++) {
            results.add(new Android(j, "random name", "random android", true, new Date(), new Date()));
        }
        return results;
    }

    public Android findAndroid(int id) {
        if (id == 1) {
            return new Android(1, "moaz korena", "Nexus 6P", true, new Date(), new Date());
        }else{
        return null;
        }
    }

    public void create(Android newPhone) {

    }

    public void edit(Android existingPhone) {

    }

    public void destroy(int parseInt) {

    }

}
