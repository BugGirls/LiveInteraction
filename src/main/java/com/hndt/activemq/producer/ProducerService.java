package com.hndt.activemq.producer;

/**
 * @author Hystar
 * @date 2018/1/5
 */
public interface ProducerService {
    /**
     * 发送消息
     *
     * @param message
     */
    void sendMessage(String message);
}
