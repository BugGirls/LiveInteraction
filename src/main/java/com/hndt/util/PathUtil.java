package com.hndt.util;

import java.time.LocalDateTime;

/**
 * @author Hystar
 * @date 2018/2/2
 */
public class PathUtil {

    /**
     * 获取当前系统文件的分隔符，因为Windows的分隔符为'\'，而linux和mac系统的分隔符为'/'
     */
    private static String separator = System.getProperty("file.separator");
    /**
     * 获取当前电脑的操作系统
     */
    private static String os = System.getProperty("os.name");

    private static final String WIN = "win";

    /**
     * 返回项目的根路径
     *
     * @return
     */
    public static String getFileBasePath() {
        String basePath = "";

        if (os.toLowerCase().startsWith(WIN)) {
            basePath = "E:/BugGirl_workspaces/IdeaProjects/LiveInteraction/target/LiveInteraction";
        } else {
            basePath = "/home/haoqf/MESSAGER/ROOT";
        }

        basePath = basePath.replace("/", separator);

        return basePath;
    }

    /**
     * 将分隔符替换成当前系统支持的分隔符
     *
     * @param url
     * @return
     */
    public static String separatorReplace(String url) {
        return url.replace("/", separator);
    }

    /**
     * 获取项目的子路径
     * 传入的参数用于路径区分
     * 路径案例：/download/voice/2018/2/2/
     *
     * @param operaType 操作类型：upload/download
     * @param fileType 文件类型：voice/image/text
     * @return
     */
    public static String getFilePath(String operaType, String fileType) {
        StringBuffer path = new StringBuffer("/" + operaType + "/" + fileType + "/");

        path.append(LocalDateTime.now().getYear() + "/");
        path.append(LocalDateTime.now().getMonthValue() + "/");
        path.append(LocalDateTime.now().getDayOfMonth() + "/");

        return path.toString().replace("/", separator);
    }

    /**
     * 获取生成xml文件路径
     *
     * 编目文件命名规范：频率名/日期/栏目名/音频名
     * 新闻频率/20160904/8030早新闻/xml/xxx.xml
     *
     * @param channelName
     * @return
     */
    public static String getXmlPath(String channelName) {
        StringBuffer path = new StringBuffer("/generate/");

        path.append(channelName + "/");
        path.append("xml/");

        return path.toString().replace("/", separator);
    }

    /**
     * 获取生成mp3文件路径
     *
     * 编目文件命名规范：频率名/日期/栏目名/音频名
     * 新闻频率/20160904/8030早新闻/mp3/xxx.mp3
     *
     * @param channelName
     * @return
     */
    public static String getMp3Path(String channelName) {
        StringBuffer path = new StringBuffer("/generate/");

        path.append(channelName + "/");
        path.append("audio/");

        return path.toString().replace("/", separator);
    }
}
