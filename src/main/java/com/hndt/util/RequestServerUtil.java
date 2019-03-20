package com.hndt.util;

import com.hndt.common.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求服务器工具类
 *
 * @author Hystar
 * @date 2018/2/8
 */
public class RequestServerUtil {

    private static Logger logger = LoggerFactory.getLogger(RequestServerUtil.class);

    /**
     * 用于请求微信服务器，即宋鎏鑫的接口地址
     *
     * @param obj 请求参数
     * @param code 操作值code
     * @return
     */
    public static String requestWeChatServer(Object obj, Integer code) {
        String operaUrL = Const.OPERATION_MESSAGE + "?ids=" + obj + "&operation_code=" + code;
        operaUrL = Base64Util.auth(operaUrL);
        String result = ClientUtil.clientJsonForGet(Const.D0_MAIN_SLX + operaUrL);
        logger.info("请求微信服务器返回结果：{}", result);
        return result;
    }
}
