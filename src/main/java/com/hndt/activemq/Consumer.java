package com.hndt.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.text.DecimalFormat;

/**
 * Created by SongLiuxin on 2018/1/4.
 */
public class Consumer {
    public static String BROKEURL = "failover://tcp://172.20.5.1:61616";//JMS
    private static final String USERNAME= ActiveMQConnection.DEFAULT_USER; // 默认的连接用户名
    private static final String PASSWORD=ActiveMQConnection.DEFAULT_PASSWORD; // 默认的连接密码
    ConnectionFactory factory;//连接工厂
    Connection connection = null;//连接
    Session session;//会话 接受或者发送消息的线程
    Destination destination;//消息的目的地

    public Consumer() throws JMSException {
        factory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public static void main(String[] args) throws JMSException {
        Consumer consumer = new Consumer();
        String stock = "a";//找到发布的Topic
        Destination destination = consumer.getSession().createTopic("STOCKS." + stock);  //创建订阅
        MessageConsumer messageConsumer = consumer.getSession().createConsumer(destination);//创建消费者

//        //同步方式获取信息
//        MapMessage map = (MapMessage) messageConsumer.receive();
//        if(map != null){
//            stock = map.getString("stock");
//            double num = map.getInt("num");
//            boolean up = map.getBoolean("up");
//            DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
//            System.out.println(stock + "\t" + df.format(num) + "\t" + (up ? "是" : "否"));
//        }
        //异步方式
        messageConsumer.setMessageListener(new Listener());
    }


    public Session getSession() {
        return session;
    }

}
