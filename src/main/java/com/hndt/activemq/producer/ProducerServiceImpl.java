package com.hndt.activemq.producer;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * @author Hystar
 * @date 2018/1/5
 */
public class ProducerServiceImpl implements ProducerService {

    @Resource
    JmsTemplate jmsTemplate;

    // 引入目的地
    @Resource(name = "topicDestination")
    Destination destination;

    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public void sendMessage(final String message) {
        // 使用JmsTemplate发送消息
        jmsTemplate.send(destination, new MessageCreator() {
            // 创建一个消息
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(message);
                return textMessage;
            }
        });
        System.out.println("发送消息：" + message);
    }
}
