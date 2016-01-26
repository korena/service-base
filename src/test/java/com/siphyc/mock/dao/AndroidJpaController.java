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
         return new ArrayList<>();
    }

    public Android findAndroid(int id) {
        return new Android(1, "moaz korena", "Nexus 6P", true, new Date(), new Date());
    }

    public void create(Android newPhone) {
        
    }

    public void edit(Android existingPhone) {
       
    }

    public void destroy(int parseInt) {
       
    }
    
    
    
}
