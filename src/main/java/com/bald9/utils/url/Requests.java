package com.bald9.utils.url;

import com.bald9.api.mimap.CookiesManage;
import com.bald9.utils.url.Exception.MaxRequestException;
import com.bald9.utils.url.Exception.TryMaxNumConnectFailException;
import com.bald9.utils.url.Exception.UnhandleCodeException;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.Function;

/**
 * @author bald9
 * @create 2022-02-02 20:52
 */
public class Requests {
    public static CookieManager cookieManager;
    public static LocalCookieStore localCookieStore;
    static {
        localCookieStore=new LocalCookieStore();
        cookieManager = new CookieManager(localCookieStore,CookiePolicy.ACCEPT_ALL);
        //cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        localCookieStore.loadCookies();
        CookieHandler.setDefault(cookieManager);
    }
    private static int connectLimitTime =9000;
    private static int readLimitTime =9000;

    public static int getConnectLimitTime() {
        return connectLimitTime;
    }

    public static void setConnectLimitTime(int connectLimitTime) {
        Requests.connectLimitTime = connectLimitTime;
    }

    public static int getReadLimitTime() {
        return readLimitTime;
    }

    public static void setReadLimitTime(int readLimitTime) {
        Requests.readLimitTime = readLimitTime;
    }

    private static String setQuery(String httpUrl, Properties query){
        if(query==null) return httpUrl;
        StringBuffer temp=new StringBuffer(httpUrl);
        boolean a=false;
        for (String key:query.stringPropertyNames()) {
            if(!a){
                a=true;
                temp.append('?');
            }else{
                temp.append('&');
            }
            temp.append(key);
            temp.append('=');
            temp.append(query.getProperty(key));
        }
        return temp.toString();
    }
    private static void setHeader(HttpURLConnection conn,Properties header){
        if(header==null) {
            header=new Properties();
        }
        if(header.getProperty("User-Agent","").equals("")){
            header.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
        }
        for (String key : header.stringPropertyNames()) {
            conn.setRequestProperty(key,header.getProperty(key));
        }
    }
    public static String getAllData(InputStream conInputStream) throws IOException {
        StringBuffer result = new StringBuffer();
        BufferedReader conBufferedReader = null;
        if (null != conInputStream) {
            conBufferedReader = new BufferedReader(new InputStreamReader(conInputStream, "UTF-8"));
            String temp = null;
            while (null != (temp = conBufferedReader.readLine())) {
                result.append(temp);
            }
        }

        return result.toString();
    }
    private static void tryMaxNum(HttpURLConnection connection) throws IOException {
        int responseCode = 0;
        int i=1;
        //开始连接
        while (i<=tryNum){
            try {
                System.out.println("连接");
                connection.connect();
                responseCode = connection.getResponseCode();
                break;
            } catch (SocketTimeoutException e) {
                if(i==tryNum){
                    //System.out.println("达到尝试次数无法连接该host");
                    throw new TryMaxNumConnectFailException(e);
                }

            } finally {
                i+=1;
            }
        }


    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Requests.debug = debug;
    }

    private static boolean debug=false;

    public static void showAllInfo(HttpURLConnection connection,String result){
        try {
            System.out.println("连接到："+connection.getURL());
            System.out.println("状态码："+connection.getResponseCode());
            System.out.println("响应内容："+result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CodeHandle codeHandle=new CodeHandle();
    static {
        codeHandle.add(200,226,requestInfo -> {
            HttpURLConnection connection = requestInfo.connection;
            InputStream conInputStream=null;
            try {
                conInputStream = connection.getInputStream();
                String result=getAllData(conInputStream);
                return new Response(result,connection.getResponseCode(),connection);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != conInputStream) {
                    try {
                        conInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        });
        codeHandle.add(300,308,requestInfo -> {
            HttpURLConnection connection = requestInfo.connection;
            String location = connection.getHeaderField("Location");
            String reHttpUrl = location;
            showAllInfo(connection,reHttpUrl);
            return request( requestInfo.method,reHttpUrl, null, null , null);
        });

    }
    public static Response request(String method,String httpUrl, Properties header, Properties query , HttpBody body)
    {
        return request( method, httpUrl,  header,  query ,  body,1);
    }
    public static int maxrequest=20;
    public static Response request(String method,String httpUrl, Properties header, Properties query , HttpBody body,int t){
        if(t>maxrequest){
            throw new MaxRequestException();
        }
        HttpURLConnection connection = null;
        InputStream conInputStream = null;
        OutputStream bodyOutputStream=null;
        String result = null;
        int responseCode = 0;
        try {
            //创建连接
            URL url = new URL(setQuery(httpUrl,query));
            connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod(method);
            //设置连接超时时间
            connection.setConnectTimeout(5000);
            //设置读取超时时间
            connection.setReadTimeout(5000);
            //DoOutput设置是否向httpUrlConnection输出，DoInput设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            //设置是否可读取
            connection.setDoOutput(true);
            connection.setDoInput(true);
            //设置通用的请求头
            setHeader(connection,header);


            if(body!=null){
                bodyOutputStream = connection.getOutputStream();
                bodyOutputStream.write(body.toString().getBytes("UTF-8"));
            }

            connection.setInstanceFollowRedirects(false);   // Make the logic below easier to detect redirections


            int i=1;
            //开始连接
            while (i<=tryNum){
                try {
                    //System.out.println("连接");
                    connection.connect();
                    responseCode = connection.getResponseCode();
                    break;
                } catch (SocketTimeoutException e) {
                    if(i==tryNum){
                        //System.out.println("达到尝试次数无法连接该host");
                        throw new TryMaxNumConnectFailException(e);
                    }

                } finally {
                    i+=1;
                }
            }

            //获取响应数据
            Response response =null;
            Function<RequestInfo, Response> fun = codeHandle.getFun(responseCode);
            if(fun!=null){
                response = fun.apply(new RequestInfo(method, httpUrl, header, query, body, connection, t));
                if(response.isHaveDeal()){
                    return response;
                }else{
                    result=response.getText();
                }
            }else{
                conInputStream = connection.getErrorStream();
                result=getAllData(conInputStream);
                response = new Response(result, responseCode, connection);
            }

            System.out.println("无法处理未通过注册Codehandle的响应:"+responseCode);
            showAllInfo(connection,result);
            throw new UnhandleCodeException(response);


            /*
            if (responseCode >= 200 && responseCode < 300) {
                //获取返回的数据
                //System.out.println("出现了200状态码："+connection.getResponseCode());
                System.out.println("不可能执行这句话");
                conInputStream = connection.getInputStream();
                result=getAllData(conInputStream);
                showAllInfo(connection,result);
            }else if(responseCode >= 100 && responseCode < 200){
                //100-199的状态码代表传输未完成时，连接时就会报错
                System.out.println("出现了100状态码："+connection.getResponseCode());
                conInputStream = connection.getErrorStream();
                result=getAllData(conInputStream);
                showAllInfo(connection,result);
            }else if(responseCode >= 300 && responseCode < 400){
                System.out.println("出现了300状态码："+connection.getResponseCode());
                String location = connection.getHeaderField("Location");
//                location = URLDecoder.decode(location, "UTF-8");
//                URL next = new URL(url, location);  // Deal with relative URLs
//                String reHttpUrl = next.toExternalForm();
                String reHttpUrl = location;

                showAllInfo(connection,reHttpUrl);
                return request( method,reHttpUrl, null, null , null);
            }else if(responseCode >= 400 && responseCode < 500){
                System.out.println("出现了400状态码："+connection.getResponseCode());
                conInputStream = connection.getErrorStream();
                result=getAllData(conInputStream);
                showAllInfo(connection,result);
            }else if(responseCode >= 500 && responseCode < 600){
                System.out.println("出现了500状态码："+connection.getResponseCode());
                conInputStream = connection.getErrorStream();
                result=getAllData(conInputStream);
                showAllInfo(connection,result);
            }
            else{
                System.out.println("出现了未知状态码："+connection.getResponseCode());
                //获取返回的数据
                conInputStream = connection.getErrorStream();
                result=getAllData(conInputStream);
                showAllInfo(connection,result);
            }
            */

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (null != bodyOutputStream) {
                try {
                    bodyOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

//            //关闭远程连接
//            if(null!=connection){
//                connection.disconnect();
//            }
        }
        return new Response(result,responseCode,connection);
    }


    public static void setTryNum(int tryNum) {
        Requests.tryNum = tryNum;
    }

    private static int tryNum=3;
    public static Response get(String httpUrl, Properties header, Properties query){
        return request("GET", httpUrl,  header,  query,null);
    }
    public static Response get(String httpUrl){
        return request("GET", httpUrl,  null,  null,null);
    }
    public static Response get(String httpUrl, Properties header){
        return request("GET", httpUrl,  header,  null,null);
    }
    public static Response getWithTs(String httpUrl){
        long ts = System.currentTimeMillis();
        Properties query = new Properties();
        query.put("ts",ts);
        return request("GET", httpUrl,  null,  query,null);
    }
    public static Response post(String httpUrl, Properties header, Properties query, HttpBody body){
        return request("POST", httpUrl,  header,  query,body);
    }
    static {
        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("https.proxyPort", "8888");
        Requests.codeHandle.add(401, CookiesManage::expireDeal);

    }


}

