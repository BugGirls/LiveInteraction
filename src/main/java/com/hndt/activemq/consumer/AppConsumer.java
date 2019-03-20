package com.hndt.activemq.consumer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Hystar
 * @date 2018/1/5
 */
public class AppConsumer {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("activemq-config/consumer.xml");
    }
}
