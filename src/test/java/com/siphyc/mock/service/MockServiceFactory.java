/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siphyc.mock.service;

import com.siphyc.service.ServiceInterface;
import com.siphyc.service.android;
import java.io.Serializable;
import javax.enterprise.inject.Produces;

/**
 *
 * @author korena
 */
public class MockServiceFactory implements Serializable{
    
    @Produces
    @android
    public ServiceInterface getAndroidService(){
    return new AndroidService();
    }
    
}
