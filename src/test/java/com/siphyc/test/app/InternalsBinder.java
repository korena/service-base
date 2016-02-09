/* 
 * The MIT License
 *
==================================================================================
 * Copyright 2016 SIPHYC SYSTEMS Sdn Bhd All Rights Reserved.
 *
 * project reference code contributed by Moaz Korena <korena@siphyc.com,moazkorena@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.siphyc.test.app;

import com.siphyc.mock.dao.AndroidJpaController;
import com.siphyc.mock.service.AndroidService;
import com.siphyc.mock.service.IphoneService;
import com.siphyc.service.ServiceInterface;
import com.siphyc.service.android;
import com.siphyc.service.iphone;
import org.glassfish.hk2.api.AnnotationLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class InternalsBinder extends AbstractBinder {

    private static class AndroidImpl extends AnnotationLiteral<android> implements android {
    }

    private static class IphoneImpl extends AnnotationLiteral<iphone> implements iphone {
    }

    @Override
    protected void configure() {
        bind(AndroidService.class).qualifiedBy(new AndroidImpl()).to(ServiceInterface.class);
        bind(IphoneService.class).qualifiedBy(new IphoneImpl()).to(ServiceInterface.class);
        bind(AndroidJpaController.class).to(AndroidJpaController.class);
    }

}
