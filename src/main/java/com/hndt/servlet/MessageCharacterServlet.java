package com.hndt.servlet;

import org.apache.activemq.web.AjaxServlet;
import org.apache.activemq.web.AjaxWebClient;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 重写 ActiveMQ Ajax 接收消息，用于设置编码格式，解决收到消息的乱码问题
 *
 * @author Hystar
 * @date 2018/2/9
 */
public class MessageCharacterServlet extends AjaxServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        super.doPost(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        super.doGet(request, response);
    }

    @Override
    protected void doMessages(AjaxWebClient client, HttpServletRequest request, HttpServletResponse response) throws JMSException, IOException {
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        super.doMessages(client, request, response);
    }

    @Override
    protected AjaxWebClient getAjaxWebClient(HttpServletRequest request) {
        AjaxWebClient client = super.getAjaxWebClient(request);
        try {
            request.setCharacterEncoding("utf-8");
            client.setUsername("zhibo");
            client.setPassword("8aj6HA");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return client;
    }

}
