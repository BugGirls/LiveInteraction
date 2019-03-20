package com.hndt.util;

import com.hndt.enums.ChannelNameEnum;
import com.hndt.enums.MessageTypeEnum;
import com.hndt.pojo.Message;
import com.hndt.vo.MessageVo;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Hystar
 * @date 2018/3/15
 */
public class XmlUtil {

    /**
     * 生成xml文件
     *
     * @param messageVo
     * @param channelName
     * @param mp3Path
     * @return
     * @throws IOException
     */
    public static String generateXmlFile(MessageVo messageVo, String channelName, String mp3Path) throws IOException {
        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);

        // 设置根节点名称
        xstream.alias("weixin", MessageVo.class);
        // 设置属性节点名称
        xstream.aliasField("duration", MessageVo.class, "total_duration");
        xstream.aliasField("nick", MessageVo.class, "nickname");
        xstream.aliasField("area", MessageVo.class, "city");
        xstream.aliasField("sendtime", MessageVo.class, "create_time");
        // 生成xml字符串
        String xmlStr = xstream.toXML(messageVo);

        // 定义xml文件生成路径：频率名/日期/栏目名/音频名（新闻频率/20160904/8030早新闻/xml/xxx.xml）
        String xmlFilePath = PathUtil.getFileBasePath() + PathUtil.getXmlPath(channelName);
        File xmlFile = new File(xmlFilePath);
        if (!xmlFile.exists()) {
            xmlFile.mkdirs();
        }
        // 将xml字符串写入到文件
        String name = generateXmlName(mp3Path) + ".xml";
        FileOutputStream fos = new FileOutputStream(xmlFilePath + name);
        fos.write(xmlStr.getBytes());
        fos.close();

        return xmlFilePath + name;
    }

    /**
     * 生成xml文件名，使用mp3文件名
     *
     * @param mp3Path
     * @return
     */
    private static String generateXmlName(String mp3Path) {
        String mp3NameSuffix = mp3Path.substring(mp3Path.lastIndexOf("/") + 1, mp3Path.length());
        String mp3Name = mp3NameSuffix.substring(0, mp3NameSuffix.indexOf("."));
        return mp3Name;
    }
}
