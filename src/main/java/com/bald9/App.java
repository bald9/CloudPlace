package com.bald9;

import com.bald9.utils.url.Requests;
import com.bald9.utils.url.Response;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        Requests.codeHandle.add(503,requestInfo -> {
            try {
                System.out.println("出现503");
                Thread.sleep(100*1);
                return Requests.request(requestInfo.method,requestInfo.httpUrl,requestInfo.header, requestInfo.query, requestInfo.body,requestInfo.t+1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch (Exception ignored){

            }
            return null;
        });
        String url = "https://httpstat.us/503";
        Response response = Requests.get(url);
        System.out.println(response.getResponseCode());
        System.out.println(response.getText());
    }
}
