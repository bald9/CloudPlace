package com.bald9;

import static org.junit.Assert.assertTrue;

import com.bald9.api.mimap.api.Login;
import com.bald9.utils.url.Requests;
import org.junit.Test;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    @Test
    public void main1(){
        HashMap<String, String> s = new HashMap<>();
        s.put(null,"nullvalue");
        for (String s1 : s.keySet()) {
            System.out.println(s1+s.get(s1));
        }
    }


}
