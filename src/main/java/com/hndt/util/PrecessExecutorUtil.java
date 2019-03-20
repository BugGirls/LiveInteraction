package com.hndt.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 音频文件处理
 *
 * @author Hystar
 * @date 2018/1/22
 */
public class PrecessExecutorUtil {

    private static final Logger logger = LoggerFactory.getLogger(PrecessExecutorUtil.class);

    /**
     * 获取当前系统路径的分隔符，因为Windows的分隔符为'\'，而linux和mac系统的分隔符为'/'
     */
    private static String separator = System.getProperty("file.separator");
    /**
     * 获取当前电脑的操作系统
     */
    private static String os = System.getProperty("os.name");

    private static final String WIN = "win";

    private static List<Process> processes = new ArrayList<>();

    private static int count = 0;

    /**
     * 通过时长截取音频文件
     *
     * 截取命令：
     * 目标文件为input.mp3，从第10秒开始截取，截取时长为5秒，将截取后的文件输出到output.mp3
     * ffmpeg -ss 10 -t 5 -i input.mp3 output.mp3
     *
     * @param startTime 开始截取时间
     * @param duration 截取时长
     * @param srcFile 源文件
     * @param type 生成的文件类型：0-临时文件、1-正式文件
     * @return 返回截取后生成的新文件名称
     */
    public static String captureFileByDuration(int startTime, int duration, String srcFile, int type) throws IOException {
        if (StringUtils.isEmpty(srcFile)) {
            return null;
        }

        File file = new File(srcFile);
        if (!file.exists()) {
            logger.info("【文件截取】源文件不存在");
            return null;
        }

        // 获取源文件路径和名称
        Path srcPath = Paths.get(srcFile);
        String srcFileName = srcPath.getFileName().toString();

        // 生成新文件名称和路径
        String newFileName;
        // 如果生成的文件类型为0，则为生成的临时文件，否则为正式文件
        if (type == 0) {
            String suffix = srcFileName.substring(srcFileName.lastIndexOf("."));
            newFileName = System.currentTimeMillis() + suffix;
        } else {
            newFileName = generateNewFileName(srcFileName);
        }
        String newFile = srcPath.getParent() + separator +  newFileName;
        logger.info("【文件截取】源文件路径：{}", srcFile);
        logger.info("【文件截取】新文件路径：{}", newFile);

        String command = ("ffmpeg -ss " + startTime + " -t " + duration + " -i " + srcPath.toString() + " " + newFile);
        logger.info("【文件截取】执行的系统命令：{}", command);
        String result = execute(command);
        exit();

        return newFile;
    }

    /**
     * 将两个截取后的音频合并成一个文件
     *
     * 合并文件，将1.mp3文件和2.mp3文件合并成一个文件12.mp3
     * ffmpeg -i "concat:1.mp3|2.mp3" -acodec copy 12.mp3
     *
     * @param file1
     * @param file2
     * @return
     */
    public static String concatFile(String srcFile, String file1, String file2) {
        if (StringUtils.isEmpty(srcFile)) {
            return null;
        }

        File file = new File(srcFile);
        if (!file.exists()) {
            logger.info("【文件合并】源文件不存在");
            return null;
        }

        Path srcPath = Paths.get(srcFile);
        String srcFileName = srcPath.getFileName().toString();
        String newFileName = generateNewFileName(srcFileName);
        String newFile = srcPath.getParent() + separator +  newFileName;

        String command = ("ffmpeg -i \"concat:" + file1 + "|" + file2 + "\" -acodec copy " + newFile);
        logger.info("【文件合并】执行的系统命令：{}", command);
        String result = execute(command);
        exit();
        logger.info("【文件合并】新文件路径：{}", newFile);

        if (!StringUtils.isEmpty(file1)) {
            file = new File(file1);
            if (file.exists()) {
                FileUtil.deleteFile(file1);
            }
        }
        if (!StringUtils.isEmpty(file2)) {
            file = new File(file2);
            if (file.exists()) {
                FileUtil.deleteFile(file2);
            }
        }

        logger.info("【文件合并】合并文件已删除");

        return newFile;
    }

    /**
     * 通过源文件名称生成新的文件名
     *
     * 生成规则：如果截取一次，file01.mp3；截取两次，file02.mp3...
     *
     * @param srcFileName
     * @return
     */
    private static String generateNewFileName(String srcFileName) {
        count++;
        String suffix = srcFileName.substring(srcFileName.lastIndexOf("."));
        String srcName = srcFileName.substring(0, srcFileName.lastIndexOf("."));
        String newName;
        if (count < 10) {
            newName = srcName + "0" + count + suffix;
        } else {
            newName = srcName + count + suffix;
        }
        return newName;
    }

    /**
     * 将mp3格式的音频通过命令转换成base64格式的数据
     *
     * 命令格式：ffmpeg -i D:test.mp3 -lavfi showwavespic=colors=gray:s=600*200 D:out.png
     *
     * @param srcFile 要转换的文件名，其中包括文件路径
     * @return
     */
    public synchronized static String executeFileWave(String srcFile) {
        if (StringUtils.isEmpty(srcFile)) {
            return null;
        }

        File file = new File(srcFile);
        if (!file.exists()) {
            logger.info("源文件不存在");
            return null;
        }

        Path srcPath = Paths.get(srcFile);
        Path path = srcPath.getParent().resolve("out.png");
        String command = ("ffmpeg -i " + srcPath.toString() + " -lavfi showwavespic=colors=gray:s=600*200 " + path.toString());
        logger.info("执行的系统命令：{}", command);
        execute(command);
        exit();

        String data = null;
        if (Files.exists(path)) {
            try {
                data = Base64.encodeBase64String(FileUtils.readFileToByteArray(path.toFile()));
                Files.delete(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    /**
     * 获取文件时长，返回的单位为秒
     *
     * @param srcFile
     * @return
     */
    public static int executeDuration(String srcFile) {
        int duration = -1;

        File file = new File(srcFile);
        if (!file.exists()) {
            logger.info("源文件不存在");
            return duration;
        }

        String command = ("ffmpeg -i " + srcFile);
        String result = execute(command);
        exit();

        String[] lines = result.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = (lines[i]);
            if (!line.contains("Duration")) {
                continue;
            }
            String[] components = line.split(",");
            for (int j = 0; j < components.length; j++) {
                String comp = components[j];
                int idx = comp.indexOf(':');
                if (idx <= 0) {
                    continue;
                }
                String name = comp.substring(0, idx).trim();
                String value = comp.substring(idx + 1).trim();
                if ("Duration".equals(name)) {
                    duration = LocalTime.parse(value).toSecondOfDay();
                }
            }
            break;
        }

        return duration;
    }

    /**
     * 通过时间截取音频文件
     *
     * 截取命令：
     * 目标文件为input.mp3，从第1秒开始，截取到第5秒，将截取后的文件拷贝到output.mp3
     * ffmpeg -i input.mp3 -ss 00:01 -to 00:05 -c copy output.mp3
     *
     * @return
     */
    public static String captureFileByTime(String startTime, String endTime, int count, String srcFile) throws IOException {
        if (StringUtils.isEmpty(srcFile)) {
            return null;
        }

        File file = new File(srcFile);
        if (!file.exists()) {
            logger.info("源文件不存在");
            return null;
        }

        Path srcPath = Paths.get(srcFile);
        String srcFileName = srcPath.getFileName().toString();
        String suffix = srcFileName.substring(srcFileName.lastIndexOf("."));
        String newFileName = System.currentTimeMillis() + suffix;
        String newFile = srcPath.getParent() + separator + newFileName;
        System.out.println("新文件路径：" + newFile);

        String command = ("ffmpeg -i " + srcPath + " -ss " + startTime + " -to " + endTime + " -c copy " + newFile);
        String result = execute(command);
        exit();

        return newFile;
    }

    /**
     * 音量调节
     *
     * ffmpeg -i input.mp3 -filter:a "volume=0.5" (-ab 320k) out.mp3
     *
     * @param type 类型，1-增加音量 0-减少音量
     * @param increment 增量，默认为0.2
     */
    public static void volumeTweaking(String srcFile, int type, double increment) throws IOException {
        if (Double.valueOf(increment) == null || increment == 0) {
            increment = 0.2;
        }

        Path srcPath = Paths.get(srcFile);
        String srcFileName = srcPath.getFileName().toString();
        String suffix = srcFileName.substring(srcFileName.lastIndexOf("."));
        String newFileName = System.currentTimeMillis() + suffix;
        String newFile = srcPath.getParent() + separator + newFileName;
        System.out.println("新文件路径：" + newFile);

        String command = "";
        if (type == 0) {
            // 减少音量
            command = ("ffmpeg -i " + srcPath.toString() + " -vol 400 " + newFile);
        } else if (type == 1) {
            // 增加音量
            command = ("ffmpeg -i " + srcPath.toString() + " -vol 400 " + newFile);
        }
        logger.info("执行的系统命令：{}", command);
        String s = execute(command);
        System.out.println(s);
        FileUtil.copyFile(new File(newFile), new File(srcFile));
        Files.delete(Paths.get(newFile));
        exit();
    }

    /**
     * 执行系统命令
     *
     * @param command
     * @return
     */
    public static String execute(String command) {
        String result = null;
        Process p = null;

        try {
            if (os.toLowerCase().startsWith(WIN)) {
                p = Runtime.getRuntime().exec(command);
            } else {
                List<String> commands = new ArrayList<>();
                commands.add("/bin/sh");
                commands.add("-c");
                commands.add(command);
                ProcessBuilder builder = new ProcessBuilder(commands);
                p = builder.start();
            }

            processes.add(p);
            if (p.waitFor(1, TimeUnit.MINUTES)) {
                InputStream stdout = p.getInputStream();
                InputStream error = p.getErrorStream();
                if (stdout.available() > 0) {
                    result = IOUtils.toString(stdout);
                }
                if (error.available() > 0) {
                    result = IOUtils.toString(error);
                }
            } else {
                p.destroyForcibly();
                processes.remove(p);
                result = "超时退出";
            }
        } catch (Exception e) {
            p.destroyForcibly();
            processes.remove(p);
            result = e.getCause().getMessage();
            e.printStackTrace();
        }
        return result;
    }

    public static void exit() {
        for (Process p : processes) {
            if (p.isAlive()) {
                p.destroyForcibly();
            }
        }
    }
}
