package com.hndt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.reflect.TypeToken;
import com.hndt.common.Const;
import com.hndt.common.ServerResponse;
import com.hndt.dao.MessageMapper;
import com.hndt.enums.*;
import com.hndt.exception.LiveInteractionException;
import com.hndt.pojo.Message;
import com.hndt.service.MessageService;
import com.hndt.util.*;
import com.hndt.vo.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RunnableFuture;

/**
 * 消息 Service
 *
 * @author Hystar
 * @date 2018/1/10
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    /**
     * 添加一条消息，如果checked为True，则自动发布消息
     *
     * @param messageVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void save(MessageVo messageVo, String channelName) throws Exception {
        if (messageVo == null) {
            throw new LiveInteractionException(ResultEnum.PARAM_EMPTY);
        }

        log.info("messageVo={}", JsonUtil.toJson(messageVo));

        // 如果是语音文件，需要下载语音
        if (!StringUtils.isEmpty(messageVo.getFile_type()) && messageVo.getFile_type().equalsIgnoreCase(MessageTypeEnum.VOICE.getValue())) {
            String filePath = downloadFile(messageVo.getContent(), channelName);
            messageVo.setContent(filePath);
        }

        // 将消息添加到本地数据库
        Message message = assembleMessage(messageVo);
        log.error("message={}", JsonUtil.toJson(message));

        // 如果为true，设置消息状态为发布状态，即自动发布消息
        if (messageVo.isChecked()) {
            String result = RequestServerUtil.requestWeChatServer(messageVo.getId(), MessageOperationEnum.PUBLISH.getCode());
            if (!StringUtils.isEmpty(result)) {
                Map<String, Object> resultMap = JsonUtil.toObject(result, new TypeToken<Map<String, Object>>() {
                }.getType());

                boolean isSuccess = (boolean) resultMap.get("success");
                if (isSuccess) {
                    message.setStatus(MessageOperationEnum.PUBLISH.getCode());
                } else {
                    throw new LiveInteractionException((String) resultMap.get("message"));
                }
            } else {
                throw new LiveInteractionException("请求服务器异常");
            }
        }

        messageMapper.insert(message);
    }

    /**
     * 通过MessageVo 填充 message
     *
     * @param messageVo
     * @return
     */
    private Message assembleMessage(MessageVo messageVo) {
        Message message = new Message();

        // 如果消息类型为语音，则调用系统命令生成Base64波形图和总时长
        if (messageVo.getFile_type().equals(MessageTypeEnum.VOICE.getValue())) {
            String data = PrecessExecutorUtil.executeFileWave(messageVo.getContent());
            message.setWave(data);
            int total = PrecessExecutorUtil.executeDuration(messageVo.getContent());
            message.setTotalDuration(total);
        }
        message.setTransactionId(messageVo.getId());
        message.setChannelId(Long.parseLong(messageVo.getChannel_id()));
        message.setAddMsg(messageVo.getAdd_msg());
        message.setCity(messageVo.getCity());
        message.setContent(messageVo.getContent());
        message.setCountry(messageVo.getCountry());
        message.setCreateTime(new Date(messageVo.getCreate_time()));
        message.setFileType(messageVo.getFile_type());
        message.setFromUid(messageVo.getFrom_uid());
        message.setIcon(messageVo.getIcon());
        message.setNickname(messageVo.getNickname());
        message.setOpenId(messageVo.getOpen_id());
        message.setProvince(messageVo.getProvince());
        message.setGender(messageVo.getSex());
        message.setSource(messageVo.getSource());
        message.setStatus(MessageOperationEnum.ORIGINAL.getCode());
        message.setUnionId(messageVo.getUnion_id());

        return message;
    }

    /**
     * 获取消息数量
     *
     * @return
     */
    @Override
    public int totalMessage(String fileType, String source, String channelId) {
        return messageMapper.totalMessage(fileType, source, channelId);
    }

    /**
     * 通过请求参数获取分页信息
     *
     * @param content
     * @param source
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> list(String content, String source, String fileType, int pageNum, int pageSize, String channelId) {
        PageHelper.startPage(pageNum, pageSize);

        List<Message> messageList = messageMapper.queryListByParam(content, source, fileType, channelId);
        PageInfo pageInfo = new PageInfo(messageList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    /**
     * 通过id删除一条消息
     *
     * @param transactionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(String transactionId) {
        if (StringUtils.isEmpty(transactionId)) {
            throw new LiveInteractionException(ResultEnum.PARAM_EMPTY);
        }

        Message message = messageMapper.queryMessageByTransactionId(transactionId);
        if (message == null) {
            throw new LiveInteractionException(ResultEnum.MESSAGE_EMPTY);
        }

        // 删除本地消息
        messageMapper.deleteByTransactionId(message.getTransactionId());

        // 删除消息服务器上的消息
        String result = RequestServerUtil.requestWeChatServer(transactionId, MessageOperationEnum.ORIGINAL.getCode());
        if (StringUtils.isEmpty(result)) {
            throw new LiveInteractionException("请求服务器异常");
        }
    }

    /**
     * 删除本地文件
     *
     * @param status
     * @param srcFile
     */
    private void deleteLocalFile(Integer status, String srcFile) {
        try {
            // 删除语音
            FileUtil.deleteFile(srcFile);

            // 如果状态为2，则说明该消息已推荐发送，生成了xml文件，删除该xml文件
            if (status == 2) {
                // 删除xml文件
                String audioFileNameSuffix = srcFile.substring(srcFile.lastIndexOf("/") + 1, srcFile.length());
                String audioFileName = audioFileNameSuffix.substring(0, audioFileNameSuffix.lastIndexOf("."));
                String xmlFileNameSuffix = audioFileName + ".xml";
                String xmlPath = srcFile.substring(0, srcFile.indexOf("/audio"));
                String xmlFile = xmlPath + "/xml/" + xmlFileNameSuffix;
                FileUtil.deleteFile(xmlFile);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除消息
     *
     * @param messageIds
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void batchDelete(String messageIds) {
        if (StringUtils.isEmpty(messageIds)) {
            throw new LiveInteractionException(ResultEnum.PARAM_EMPTY);
        }

        List<Long> ids = JsonUtil.toList(messageIds, Long.class);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < ids.size(); i++) {
            stringBuffer.append(ids.get(i));
            if ((ids.size() - 1) != i) {
                stringBuffer.append(",");
            }
        }

        // 批量删除本地消息
        messageMapper.batchDelete(ids);

        // 批量删除服务器消息
        String result = RequestServerUtil.requestWeChatServer(stringBuffer.toString(), MessageOperationEnum.ORIGINAL.getCode());
        if (StringUtils.isEmpty(result)) {
            throw new LiveInteractionException("请求服务器异常");
        }
    }

    /**
     * 通过id获取消息详情
     *
     * @param messageId
     * @return
     */
    @Override
    public Message detail(String messageId) {
        if (StringUtils.isEmpty(messageId)) {
            throw new LiveInteractionException(ResultEnum.PARAM_EMPTY);
        }

        Message message = messageMapper.selectByPrimaryKey(Long.parseLong(messageId));
        if (message == null) {
            throw new LiveInteractionException(ResultEnum.MESSAGE_EMPTY);
        } else {
            return message;
        }
    }

    /**
     * 发布一条或多条消息
     *
     * @param messageIds
     * @return
     */
    @Override
    public void batchPublishMessage(String messageIds) {
        if (StringUtils.isEmpty(messageIds)) {
            throw new LiveInteractionException(ResultEnum.PARAM_EMPTY);
        }

        List<Long> ids = JsonUtil.toList(messageIds, Long.class);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < ids.size(); i++) {
            stringBuffer.append(ids.get(i));
            if ((ids.size() - 1) != i) {
                stringBuffer.append(",");
            }
        }

        String result = RequestServerUtil.requestWeChatServer(stringBuffer.toString(), MessageOperationEnum.PUBLISH.getCode());
        if (!StringUtils.isEmpty(result)) {
            Map<String, Object> resultMap = JsonUtil.toObject(result, new TypeToken<Map<String, Object>>() {
            }.getType());

            boolean isSuccess = (boolean) resultMap.get("success");
            if (isSuccess) {
                messageMapper.batchPublishMessage(ids);
            } else {
                throw new LiveInteractionException((String) resultMap.get("message"));
            }
        } else {
            throw new LiveInteractionException(ResultEnum.SERVER_ERROR);
        }
    }

    /**
     * 裁剪音频文件
     *
     * @param startTime 开始时间
     * @param duration  裁剪时长
     * @param srcFile   源文件
     * @return
     * @throws IOException
     */
    @Override
    public ServerResponse radioTailor(Integer startTime, Integer duration, String srcFile) {
        if (StringUtils.isEmpty(srcFile) || startTime == null || duration == null) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        Map<String, Object> resultMap = new HashMap<>();
        // 通过系统命令裁剪音频，返回新文件路径
        try {
            // 获取总时长
            int totalDuration = PrecessExecutorUtil.executeDuration(srcFile);

            // 如果开始时间为0，且开始时间与时长的和大于总时长，则为截取的全部
            if (startTime == 0 && ((startTime + duration) >= totalDuration)) {
                String wave = PrecessExecutorUtil.executeFileWave(srcFile);
                resultMap.put("wave", wave);
                resultMap.put("total", totalDuration);
                resultMap.put("srcFile", srcFile);
            }
            // 如果开始时间为0，且开始时间与时长的和小于总时长，则为从开始截取到中间的某个部分
            else if (startTime == 0 && ((startTime + duration) < totalDuration)) {
                String newFile = PrecessExecutorUtil.captureFileByDuration(duration, totalDuration, srcFile, Const.FILE_TYPE_FORMAL);
                String wave = PrecessExecutorUtil.executeFileWave(newFile);
                int total = PrecessExecutorUtil.executeDuration(newFile);
                resultMap.put("wave", wave);
                resultMap.put("total", total);
                resultMap.put("srcFile", newFile);
            }
            // 如果开始时间不为0，且开始时间与时长的和等于总时长，则为从中间的某个地方开始，截取到结尾
            else if (startTime != 0 && ((startTime + duration) >= totalDuration)) {
                String newFile = PrecessExecutorUtil.captureFileByDuration(0, startTime, srcFile, Const.FILE_TYPE_FORMAL);
                String wave = PrecessExecutorUtil.executeFileWave(newFile);
                int total = PrecessExecutorUtil.executeDuration(newFile);
                resultMap.put("wave", wave);
                resultMap.put("total", total);
                resultMap.put("srcFile", newFile);
            }
            // 最后一种情况为截取中间的某一段
            else {
                String file1 = PrecessExecutorUtil.captureFileByDuration(0, startTime, srcFile, Const.FILE_TYPE_TEMP);
                String file2 = PrecessExecutorUtil.captureFileByDuration((startTime + duration), totalDuration, srcFile, Const.FILE_TYPE_TEMP);
                String newFile = PrecessExecutorUtil.concatFile(srcFile, file1, file2);
                String wave = PrecessExecutorUtil.executeFileWave(newFile);
                int total = PrecessExecutorUtil.executeDuration(newFile);
                resultMap.put("wave", wave);
                resultMap.put("total", total);
                resultMap.put("srcFile", newFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }

        return ServerResponse.createBySuccess(resultMap);
    }

    /**
     * 音频的撤回
     *
     * @param srcFile
     * @param nowFile
     * @return
     */
    @Override
    public ServerResponse radioRepeal(String srcFile, String nowFile) {
        if (StringUtils.isEmpty(srcFile) || StringUtils.isEmpty(nowFile)) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        srcFile = PathUtil.separatorReplace(srcFile);
        nowFile = PathUtil.separatorReplace(nowFile);

        if (srcFile.equals(nowFile)) {
            return ServerResponse.createBySuccess();
        } else {
            String suffix = nowFile.substring(nowFile.lastIndexOf("."));
            String nowFileName = nowFile.substring(0, nowFile.lastIndexOf("."));
            String newFile = nowFileName.substring(0, nowFileName.length() - 2) + suffix;
            String wave = PrecessExecutorUtil.executeFileWave(newFile);
            int total = PrecessExecutorUtil.executeDuration(newFile);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("wave", wave);
            resultMap.put("total", total);
            resultMap.put("srcFile", newFile);

            FileUtil.deleteFile(nowFile);
            return ServerResponse.createBySuccess(resultMap);
        }
    }

    /**
     * 保存裁剪后的音频文件
     *
     * @param srcFile
     * @param nowFile
     * @return
     * @throws IOException
     */
    @Override
    public ServerResponse radioSave(String srcFile, String nowFile, String messageId) {
        if (StringUtils.isEmpty(srcFile) || StringUtils.isEmpty(nowFile)) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.PARAM_EMPTY.getCode(), ResultEnum.PARAM_EMPTY.getValue());
        }

        try {
            srcFile = PathUtil.separatorReplace(srcFile);
            nowFile = PathUtil.separatorReplace(nowFile);

            if (srcFile.equals(nowFile)) {
                return ServerResponse.createBySuccess();
            } else {
                // 将最后一次裁剪后的文件复制到源文件
                FileUtil.copyFile(new File(nowFile), new File(srcFile));

                while (true) {
                    FileUtil.deleteFile(nowFile);

                    String suffix = nowFile.substring(nowFile.lastIndexOf("."));
                    String nowFileName = nowFile.substring(0, nowFile.lastIndexOf("."));
                    String newFile = nowFileName.substring(0, nowFileName.length() - 2) + suffix;
                    nowFile = newFile;
                    if (srcFile.equals(nowFile)) {
                        break;
                    }
                }
                String wave = PrecessExecutorUtil.executeFileWave(srcFile);
                int total = PrecessExecutorUtil.executeDuration(srcFile);

                Message message = messageMapper.selectByPrimaryKey(Long.parseLong(messageId));
                if (message == null) {
                    return ServerResponse.createByErrorMessage("音频文件保存失败");
                } else {
                    message.setWave(wave);
                    message.setTotalDuration(total);
                    messageMapper.updateByPrimaryKey(message);
                }

                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("wave", wave);
                resultMap.put("total", total);
                resultMap.put("srcFile", srcFile);

                return ServerResponse.createBySuccess(resultMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    /**
     * 音频音量调整
     *
     * @param srcFile
     * @param type
     * @return
     */
    @Override
    public ServerResponse volumeTweak(String srcFile, Integer type) {
        try {
            PrecessExecutorUtil.volumeTweaking(srcFile, type, 0);
            return ServerResponse.createBySuccess();
        } catch (IOException e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    /**
     * 下载文件到指定路径
     *
     * @param hostUrl     需要下载的源文件的网络路径
     * @param channelName 频率名称
     * @return 文件生成的路径
     */
    @Override
    public String downloadFile(String hostUrl, String channelName) throws Exception {
        URL server = new URL(hostUrl);
        String newFile = PathUtil.getFileBasePath() + PathUtil.getMp3Path(ChannelNameEnum.keyOf(channelName).getValue()) + System.currentTimeMillis() + ".mp3";
        File file = new File(newFile);
        FileUtils.copyURLToFile(server, file);
        return file.getAbsolutePath();
    }

    /**
     * 推荐发送
     *
     * @param content
     * @param transactionId
     * @param channelName
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recommendSendMessage(String content, String transactionId, String channelName) throws IOException {
        if (StringUtils.isEmpty(transactionId) || StringUtils.isEmpty(channelName)) {
            throw new LiveInteractionException(ResultEnum.PARAM_EMPTY);
        }

        Message message = messageMapper.queryMessageByTransactionId(transactionId);
        if (message == null) {
            throw new LiveInteractionException(ResultEnum.MESSAGE_EMPTY);
        }

        // 1、如果是文本文件，允许修改内容
        if (message.getFileType().equalsIgnoreCase(MessageTypeEnum.TEXT.getValue())) {
            // 将消息更新到本地数据库
            message.setContent(content);
            messageMapper.updateByPrimaryKey(message);
        }

        // 2、如果消息类型是语音，需要生成xml文件
        if (message.getFileType().equalsIgnoreCase(MessageTypeEnum.VOICE.getValue())) {
            MessageVo messageVo = new MessageVo();

            messageVo.setRemark("null");
            messageVo.setNickname(message.getContent());
            messageVo.setContent(message.getAddMsg());
            messageVo.setTotal_duration(message.getTotalDuration() * 1000);
            messageVo.setSendTime(DateUtil.dateToString(message.getCreateTime()));
            messageVo.setCity((StringUtils.isEmpty(message.getCity()) ? "" : message.getCity()) + (StringUtils.isEmpty(message.getProvince()) ? "" : message.getProvince()));
            messageVo.setSource(MessageSourceEnum.keyOf(message.getSource()).getValue() + "-" + message.getNickname());
            String filePath = message.getContent();
            filePath = filePath.substring(filePath.indexOf("/audio"), filePath.length());
            messageVo.setFilePath(filePath);

            // 生成xml文件
            channelName = ChannelNameEnum.keyOf(channelName).getValue();
            String s = XmlUtil.generateXmlFile(messageVo, channelName, message.getContent());
            System.out.println(s);
        }

        // 3、推荐消息
        // 判断消息状态是否正确
        if (message.getStatus().equals(MessageOperationEnum.RECOMMEND.getCode())) {
            throw new LiveInteractionException(ResultEnum.MESSAGE_IS_RECOMMEND);
        }
        // 更改消息服务器上的消息状态为 推荐：code=2
        String result = RequestServerUtil.requestWeChatServer(transactionId, MessageOperationEnum.RECOMMEND.getCode());
        if (!StringUtils.isEmpty(result)) {
            Map<String, Object> resultMap = JsonUtil.toObject(result, new TypeToken<Map<String, Object>>() {
            }.getType());

            boolean isSuccess = (boolean) resultMap.get("success");
            if (isSuccess) {
                messageMapper.updateStatusByTransactionId(transactionId, MessageOperationEnum.RECOMMEND.getCode());
            } else {
                throw new LiveInteractionException((String) resultMap.get("message"));
            }
        } else {
            throw new LiveInteractionException("请求服务器异常，请稍后重试");
        }
    }

}
