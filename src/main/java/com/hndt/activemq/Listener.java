package com.hndt.activemq;

import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.text.DecimalFormat;

/**
 * Created by SongLiuxin on 2018/1/4.
 */
public class Listener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            if(message instanceof ActiveMQTextMessage){
                ActiveMQTextMessage textMessage = (ActiveMQTextMessage) message;
                System.out.println("收到的消息:" + textMessage.getText());
            }
            if(message instanceof MapMessage){
                MapMessage map = (MapMessage) message;
                String stock = map.getString("stock");
                double num = map.getInt("num");
                boolean up = map.getBoolean("up");
                DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
                System.out.println(stock + "\t" + df.format(num) + "\t" + (up ? "是" : "否"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
