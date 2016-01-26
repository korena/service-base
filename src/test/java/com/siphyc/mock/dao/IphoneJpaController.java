/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siphyc.mock.dao;

import com.siphyc.model.Iphone;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author korena
 */
public class IphoneJpaController {

    public List<Iphone> findIphoneEntities(int limit, int i) {
        return new ArrayList<>();
    }

    public Iphone findIphone(int id) {
        return new Iphone(2, "Moaz korena", "6", true, new Date(), new Date());
    }

    public void create(Iphone newPhone) {

    }

    public void edit(Iphone existingPhone) {

    }

    public void destroy(int parseInt) {

    }
    
}
