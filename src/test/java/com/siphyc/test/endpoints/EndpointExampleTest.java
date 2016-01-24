/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siphyc.test.endpoints;

import com.siphyc.test.app.CdiEnabled;
import static org.hamcrest.CoreMatchers.containsString;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author korena
 */
public class EndpointExampleTest extends CdiEnabled {

    @Test
    public void AndroidResourceTest() {
        String s = target().path("resource/android").request().get(String.class);
        assertThat(s, containsString("unsupported request"));
    }
}
