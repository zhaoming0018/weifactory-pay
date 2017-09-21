package net.vastsum.weifactory.wechatpay.utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UrlRequest {
    /**
     * 发送get请求，返回结果字符串
     * @param url 请求的地址
     * @return 返回的字符串
     * @throws Exception IOException
     */
    public static String getRequest(String url) throws Exception{
        HttpUriRequest httpGet = new HttpGet(url);
        return getHttpResult(httpGet);
    }

    /**
     * 发送post请求，返回结果字符串
     * @param url 请求的地址
     * @param postData 请求的xml字符串
     * @return 返回的字符串
     * @throws Exception IOException
     */
    public static String postRequest(String url,String postData) throws Exception{
        HttpEntity httpEntity = new StringEntity(postData,"UTF-8");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(httpEntity);
        return getHttpResult(httpPost);
    }

    /**
     * 发送请求
     * @param httpRequest 请求对象
     * @return 请求结果
     */
    private static String getHttpResult(HttpUriRequest httpRequest){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse response = httpClient.execute(httpRequest);
            HttpEntity responeEntity = response.getEntity();
            InputStream in = responeEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine())!=null){
                sb.append(line).append("\n");
            }
            in.close();
            return sb.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
