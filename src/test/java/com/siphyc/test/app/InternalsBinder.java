/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siphyc.test.app;

import com.siphyc.mock.service.AndroidService;
import com.siphyc.mock.service.IphoneService;
import com.siphyc.service.ServiceInterface;
import com.siphyc.service.android;
import com.siphyc.service.iphone;
import org.glassfish.hk2.api.AnnotationLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 *
 * @author korena
 */
public class InternalsBinder extends AbstractBinder {

    private static class AndroidImpl extends AnnotationLiteral<android> implements android {
    }

    private static class IphoneImpl extends AnnotationLiteral<iphone> implements iphone {
    }

    @Override
    protected void configure() {
        bind(AndroidService.class).qualifiedBy(new AndroidImpl()).to(ServiceInterface.class);
        bind(IphoneService.class).qualifiedBy(new IphoneImpl()).to(ServiceInterface.class);
    }

}
