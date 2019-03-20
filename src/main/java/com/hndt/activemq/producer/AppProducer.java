package com.hndt.activemq.producer;

import com.hndt.enums.MessageSourceEnum;
import com.hndt.enums.MessageTypeEnum;
import com.hndt.enums.StatusEnum;
import com.hndt.pojo.Manager;
import com.hndt.pojo.Message;
import com.hndt.util.JsonUtil;
import com.hndt.vo.MessageVo;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hystar
 * @date 2018/1/5
 */
public class AppProducer implements Runnable {

    private static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) {
        AppProducer appProducer = new AppProducer();
        Thread thread = new Thread(appProducer);
        thread.start();
    }

    @Override
    public void run() {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("activemq-config/producer.xml");
            ProducerService producerService = context.getBean(ProducerService.class);

            MessageVo messageVo = new MessageVo();
            messageVo.setSendTime(System.currentTimeMillis() + "");
            messageVo.setSource("WEI_XIN");
            messageVo.setContent("这个一个测试");
            messageVo.setFilePath("D:/ee.mp3");
            messageVo.setChannel_id("25");
            messageVo.setCity("驻马店");
            messageVo.setCountry("中国");
            messageVo.setCreate_time(System.currentTimeMillis());
            messageVo.setFile_type("TEXT");
            messageVo.setIcon("xxx");
            messageVo.setNickname("仰望");
            messageVo.setOpen_id("oaYgpwEGraVStZ1Mpb0Uxov2rH6Y");
            messageVo.setProvince("峄城区");
            messageVo.setSex(1);

            producerService.sendMessage(JsonUtil.toJson(messageVo));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
