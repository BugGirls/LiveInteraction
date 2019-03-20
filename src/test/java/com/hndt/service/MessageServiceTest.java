package com.hndt.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author Hystar
 * @date 2018/3/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/dispatcher-servlet.xml", "classpath:/applicationContext.xml"})
public class MessageServiceTest {

    @Resource
    private MessageService messageService;

    @Test
    public void recommendSendMessageTest() throws IOException {
        String transactionId = "27289";
        String channelName = "星河音乐台";
        messageService.recommendSendMessage(null, transactionId, channelName);
    }
}