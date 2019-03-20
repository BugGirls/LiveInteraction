package com.hndt.test;

import com.hndt.util.FileUtil;
import com.hndt.util.PrecessExecutorUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hystar
 * @date 2018/1/9
 */
public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {

//        PrecessExecutorUtil.formatToMP3("C:\\Users\\songci\\IdeaProjects\\f25a7d66a6584b3b896efc674127d866.amr");

        deleteLocalFile("E:/BugGirl_workspaces/IdeaProjects/LiveInteraction/src/1061/audio/1518423967557.mp3");
    }

    private static void deleteLocalFile(String srcFile) {
        // 删除语音文件
        FileUtil.deleteFile(srcFile);

        // 删除xml文件
        String audioFileNameSuffix = srcFile.substring(srcFile.lastIndexOf("/") + 1, srcFile.length());
        String audioFileName = audioFileNameSuffix.substring(0, audioFileNameSuffix.lastIndexOf("."));
        String xmlFileNameSuffix = audioFileName + ".xml";
        String xmlPath = srcFile.substring(0, srcFile.indexOf("/audio"));
        String xmlFile = xmlPath + "/xml/" + xmlFileNameSuffix;
        FileUtil.deleteFile(xmlFile);
    }

}
