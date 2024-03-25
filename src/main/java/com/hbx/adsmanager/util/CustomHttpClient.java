package com.hbx.adsmanager.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

public class CustomHttpClient {

    public static String postRequest(String url, String cookie,String requestBody) throws InterruptedException{

        String responseBody = "";
        //1.生成httpclient，相当于该打开一个浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        // 设置请求头信息
        httpPost.setHeader("Accept", "application/json, text/plain, */*");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpPost.setHeader("Connection", "keep-alive");
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setHeader("Host", "sino-gateway.sinoclick.com");
        httpPost.setHeader("Origin", "https://business.sinoclick.com");
        httpPost.setHeader("Referer", "https://business.sinoclick.com/");
        httpPost.setHeader("Sec-Ch-Ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"");
        httpPost.setHeader("Sec-Ch-Ua-Mobile", "?0");
        httpPost.setHeader("cookie",cookie);
        httpPost.setHeader("Sec-Ch-Ua-Platform", "\"Windows\"");
        httpPost.setHeader("Sec-Fetch-Dest", "empty");
        httpPost.setHeader("Sec-Fetch-Mode", "cors");
        httpPost.setHeader("Sec-Fetch-Site", "same-site");
        httpPost.setHeader("User-Agent", UAUtil.generateRandomPCUserAgent());

        //创建请求
        CloseableHttpResponse response = null;
        // 处理响应
        try {
            // 设置请求体
            StringEntity entity = new StringEntity(requestBody);
            httpPost.setEntity(entity);
            // 执行请求
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200) {
                // 将响应内容转换为字符串
                responseBody = EntityUtils.toString(responseEntity);
                // 打印响应内容
                System.out.println("Response: " + responseBody);
            }else {
                return "";
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseBody;
    }

    public static String getRequest(String url, String cookie) throws InterruptedException {

        //1.生成httpclient，相当于该打开一个浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("cookie", cookie);
        httpGet.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        httpGet.addHeader("x-sino-domain", "sinoclick");
        CloseableHttpResponse response = null;
        String content = null;
        try {
            //使用httpClient发起请求 获取 response
            response = httpClient.execute(httpGet);

            //解析响应
            if (response.getStatusLine().getStatusCode() == 200) {
                content = EntityUtils.toString(response.getEntity(), "utf8");
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
