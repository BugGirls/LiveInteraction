package com.hndt.util;

import com.hndt.common.Const;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.CharEncoding;

import java.io.UnsupportedEncodingException;

/**
 * Base64 工具类
 *
 * @author Hystar
 * @date 2018/1/17
 */
public class Base64Util {

    /**
     * 将传入的文本进行Base64加密
     *
     * @param text
     * @return
     */
    public static String base64Decode(String text) {
        String result = null;
        byte[] b= new byte[0];
        try {
            b = org.apache.commons.codec.binary.Base64.decodeBase64(text.getBytes(CharEncoding.UTF_8));
            result=new String(b, CharEncoding.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String auth(String url) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        url = url + (url.indexOf('?') > 0 ? "&" : "?");
        String url2 = String.format("%stimestamp=%s&appID=%s&appSecret=%s", url, timestamp, Const.APP_ID, Const.APP_SECRET);
        String token = DigestUtils.sha1Hex(url2);
        url2 = String.format("%stimestamp=%s&appID=%s&token=%s", url, timestamp, Const.APP_ID, token);
        return url2;
    }
}
