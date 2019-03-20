package com.hndt.activemq.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hystar
 * @date 2018/1/10
 */

public class ConsumerMessageListener implements MessageListener {

    public static List messages;

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;

            try {
                System.out.println("接收消息：" + textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        if (messages==null){
            messages = new ArrayList<>();
        }
        messages.add(message);
    }


}
