package com.sunrisejay.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.Arrays;
import java.util.List;

import static com.sunrisejay.constant.Constant.*;

/**
 * @auther Sunrise_Jay
 */
public class HttpClientUtil {

    public static String doPost(String targetApi, String bestNode, CookieStore cookieStore, List<BasicNameValuePair> formParams) throws Exception {

        // 1. 创建 RequestConfig 对象
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000) // 设置套接字超时（单位：毫秒）
                .setConnectTimeout(30000) // 设置连接超时（单位：毫秒）
                .setConnectionRequestTimeout(30000) // 设置从连接池获取连接的超时时间（单位：毫秒）
                .build();


        // 4. 定制 CloseableHttpClient
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore) // 设置默认的 CookieStore
                .setDefaultRequestConfig(requestConfig) // 设置默认的 RequestConfig
                .setDefaultHeaders(Arrays.asList(
                        new BasicHeader("Accept", ACCEPT),
                        new BasicHeader("Content-Type", CONTENT_TYPE),
                        new BasicHeader("Origin", NODE),
                        new BasicHeader("Referer", NODE),
                        new BasicHeader("User-Agent", USER_AGENT),
                        new BasicHeader("X-Requested-With", XML_HTTP_REQUEST)
                ))
                .build();

        // 使用 httpClient 进行 HTTP 请求...
        HttpPost httpPost = new HttpPost(bestNode + targetApi);

        //请求体转化表单
        HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
        httpPost.setEntity(entity);

        //执行请求
        HttpResponse httpResponse = httpClient.execute(httpPost);

        String content = EntityUtils.toString(httpResponse.getEntity());


        System.out.println("地址:" + targetApi + "响应内容：" + content);


        httpClient.close();

        return content;
    }

    public static String doGet(String targetApi, String bestNode, CookieStore cookieStore) throws Exception {


        // 1. 创建 RequestConfig 对象
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000) // 设置套接字超时（单位：毫秒）
                .setConnectTimeout(30000) // 设置连接超时（单位：毫秒）
                .setConnectionRequestTimeout(30000) // 设置从连接池获取连接的超时时间（单位：毫秒）
                .build();


        // 4. 定制 CloseableHttpClient
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore) // 设置默认的 CookieStore
                .setDefaultRequestConfig(requestConfig) // 设置默认的 RequestConfig
                .setDefaultHeaders(Arrays.asList(
                        new BasicHeader("Accept", ACCEPT),
                        new BasicHeader("Content-Type", CONTENT_TYPE),
                        new BasicHeader("Origin", NODE),
                        new BasicHeader("Referer", NODE),
                        new BasicHeader("User-Agent", USER_AGENT),
                        new BasicHeader("X-Requested-With", XML_HTTP_REQUEST)
                ))
                .build();

        // 使用 httpClient 进行 HTTP 请求...
        HttpGet httpGet = new HttpGet(bestNode + targetApi);


        //执行请求
        HttpResponse httpResponse = httpClient.execute(httpGet);

        String content = EntityUtils.toString(httpResponse.getEntity());


        //System.out.println("地址:" + url + "响应内容：" + content);


        httpClient.close();


        return content;
    }


}
