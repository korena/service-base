/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siphyc.dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author korena
 */
public class JPAcommonUtils {

    private static final String PERSISTENCE_UNIT_NAME = "persistenceUnit";
    private static final EntityManagerFactory factory;
    static{
    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
    public static EntityManagerFactory getFacory(){
    return factory;
    }
}