package com.hndt.util;

import okhttp3.*;
import okhttp3.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * http连接工具类
 *
 * @author Hystar
 * @date 2017/11/12
 */
public class ClientUtil {

    private static final MediaType FORM_CONTENT_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Logger logger = LoggerFactory.getLogger(ClientUtil.class);

    /**
     * 传入URL进行http连接，用于get请求
     *
     * @param url
     * @return
     */
    public static String clientJsonForGet(String url) {
        String result = "";

        // 创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        // 创建一个请求对象
        Request request = new Request.Builder().url(url).get().build();
        // 发送请求获取响应
        try {
            Response response = okHttpClient.newCall(request).execute();
            // 判断请求是否成功
            if (response.isSuccessful()) {
                // 打印服务端返回结果
                result = response.body().string();
            }
        } catch (IOException e) {
            logger.error("【发送HTTP请求】失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 传入URL进行http连接，用于发送JSON格式的数据
     *
     * @param json
     * @param url
     * @return
     */
    public static String clientJson(String json, String url) {
        String result = "";

        // 创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        // 创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(FORM_CONTENT_TYPE_JSON, json);
        // 创建一个请求对象
        Request request = new Request.Builder().url(url).post(requestBody).build();
        // 发送请求获取响应
        try {
            Response response = okHttpClient.newCall(request).execute();
            // 判断请求是否成功
            if (response.isSuccessful()) {
                // 打印服务端返回结果
                result = response.body().string();
            }
        } catch (IOException e) {
            logger.error("【发送HTTP请求】失败：" + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
}
