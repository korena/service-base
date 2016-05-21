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
package com.siphyc.mock.dao;

import com.siphyc.dao.exceptions.NonexistentEntityException;
import com.siphyc.dao.exceptions.RollbackFailureException;
import com.siphyc.model.Android;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

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

    public void create(Android newPhone) throws RollbackFailureException, Exception {
        String model = newPhone.getModel();
        if (Utils.contains(model, "Iphone")){
        throw new RollbackFailureException("test roll back failure exception");
        } else if (!Utils.contains(model, "Android")){
        throw new Exception("test random exception");
        }
    }

    public void edit(Android existingPhone) throws NonexistentEntityException, RollbackFailureException, Exception {
        int id = existingPhone.getId();
        if(id == 2){
        throw new NonexistentEntityException("test non existant exception");
        } else if (id == 3){
        throw new RollbackFailureException("test roll back failure exception");
        } else if (id == 4){
        throw new Exception("test random exception");
        }
    }

    public void destroy(int id) throws NonexistentEntityException, RollbackFailureException, Exception {

        if(id == 2){
        throw new NonexistentEntityException("test non existant exception");
        } else if (id == 3){
        throw new RollbackFailureException("test roll back failure exception");
        } else if (id == 4){
        throw new Exception("test random exception");
        }
        
    }

}
