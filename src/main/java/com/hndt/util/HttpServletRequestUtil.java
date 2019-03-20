package com.hndt.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理HTTPServletRequest参数
 *
 * @author Hystar
 * @date 2017/10/27
 */
public class HttpServletRequestUtil {

    /**
     * 将String类型的数字转换成int型
     *
     * @param request
     * @param key
     * @return
     */
    public static int getInt(HttpServletRequest request, String key) {
        try {
            // static Integer decode(String nm) ：将字符串转换为整数。
            return Integer.decode(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 将String类型的数字转换成double型
     *
     * @param request
     * @param key
     * @return
     */
    public static Double getDouble(HttpServletRequest request, String key) {
        try {
            return Double.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1d;
        }
    }

    /**
     * 将String类型的数字转换成Long型
     *
     * @param request
     * @param key
     * @return
     */
    public static long getLong(HttpServletRequest request, String key) {
        try {
            return Long.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 将String类型的数字转换成boolean型
     *
     * @param request
     * @param key
     * @return
     */
    public static boolean getBoolean(HttpServletRequest request, String key) {
        try {
            return Boolean.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return false;
        }
    }

    public static String getString(HttpServletRequest request, String key) {
        try {
            String result = request.getParameter(key);
            if (result != null) {
                result = result.trim();
            } else {
                result = null;
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
