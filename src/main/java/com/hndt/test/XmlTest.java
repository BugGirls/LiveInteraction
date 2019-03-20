package com.hndt.test;

import com.hndt.pojo.Message;
import com.hndt.vo.MessageVo;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Hystar
 * @date 2018/2/6
 */
public class XmlTest {

    public static List<MessageVo> getMessageVoList() {
        MessageVo messageVo = new MessageVo();
        messageVo.setSource("WEI_XIN");
        messageVo.setContent("null");
        messageVo.setCity("郑州");
        messageVo.setFilePath("E:test.mp3");
        messageVo.setNickname("仰望");
        messageVo.setCreate_time(System.currentTimeMillis());
        messageVo.setTotal_duration(110);

        MessageVo messageVo2 = new MessageVo();
        messageVo2.setSource("WEI_XIN");
        messageVo2.setContent(null);
        messageVo2.setCity("驻马店");
        messageVo2.setFilePath("D:demo.mp3");
        messageVo2.setNickname("empress");
        messageVo2.setCreate_time(System.currentTimeMillis());
        messageVo2.setTotal_duration(60);

        List<MessageVo> messageVoList = new ArrayList<>();
        messageVoList.add(messageVo);
        messageVoList.add(messageVo2);

        return messageVoList;
    }

    public static void main(String[] args) throws IOException {
        MessageVo messageVo2 = new MessageVo();
        messageVo2.setSource("WEI_XIN");
        messageVo2.setContent("null");
        messageVo2.setCity("驻马店");
        messageVo2.setFilePath("D:demo.mp3");
        messageVo2.setNickname("empress");
        messageVo2.setCreate_time(System.currentTimeMillis());
        messageVo2.setTotal_duration(60);
        messageVo2.setRemark("null");

        XStream xstream = new XStream();
        xstream.alias("weixin", MessageVo.class);
        xstream.aliasField("duration", MessageVo.class, "total_duration");
        xstream.aliasField("nick", MessageVo.class, "nickname");
        xstream.aliasField("area", MessageVo.class, "city");
        xstream.aliasField("sendtime", MessageVo.class, "create_time");
        String s = xstream.toXML(messageVo2);
      try {
        String filePath = "D:/t.txt";
          FileOutputStream fos = new FileOutputStream(filePath);
          fos.write(s.getBytes());
          fos.close();
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
}
