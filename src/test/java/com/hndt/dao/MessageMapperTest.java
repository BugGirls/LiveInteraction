package com.hndt.dao;

import com.hndt.enums.MessageTypeEnum;
import com.hndt.pojo.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Hystar
 * @date 2018/3/19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/dispatcher-servlet.xml", "classpath:/applicationContext.xml"})
public class MessageMapperTest {

    @Resource
    private MessageMapper messageMapper;

    @Test
    public void testTotalMessage() throws Exception {
        int total = messageMapper.totalMessage(MessageTypeEnum.VOICE.getValue(), null, null);
        System.out.println(total);
    }

    @Test
    public void queryListByParamTest() {
        List<Message> messageList = messageMapper.queryListByParam(null, null, null, null);
        System.out.println(messageList.size());
    }
}