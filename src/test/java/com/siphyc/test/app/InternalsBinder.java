/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siphyc.test.app;

import com.siphyc.mock.service.AndroidService;
import com.siphyc.service.ServiceInterface;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 *
 * @author korena
 */
public class InternalsBinder extends AbstractBinder {

    @Override
    protected void configure() {
//        bind(AndroidService.class).to(ServiceInterface.class);
    }
}
