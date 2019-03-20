package com.hndt.service;

import com.github.pagehelper.PageInfo;
import com.hndt.common.ServerResponse;
import com.hndt.pojo.Message;
import com.hndt.vo.MessageVo;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @author Hystar
 * @date 2018/1/10
 */
public interface MessageService {

    /**
     * 添加一条消息
     *
     * @param messageVo
     * @param channelName
     * @return
     * @throws Exception
     */
    void save(MessageVo messageVo, String channelName) throws Exception;

    /**
     * 通过请求参数获取分页信息
     *
     * @param content
     * @param source
     * @param fileType
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> list(String content, String source, String fileType, int pageNum, int pageSize, String channelId);
    /**
     * 通过id删除一条消息
     *
     * @param transactionId
     * @return
     */
    void delete(String transactionId);
    /**
     * 通过id获取消息详情
     *
     * @param messageId
     * @return
     */
    Message detail(String messageId);

    /**
     * 批量删除消息
     *
     * @param messageIds
     * @return
     */
    void batchDelete(String messageIds);

    /**
     * 裁剪音频文件
     *
     * @param startTime 开始时间
     * @param duration 裁剪时长
     * @param srcFile 源文件
     * @return
     * @throws IOException
     */
    ServerResponse radioTailor(Integer startTime, Integer duration, String srcFile);

    /**
     * 音频的撤回
     *
     * @param srcFile
     * @param nowFile
     * @return
     */
    ServerResponse radioRepeal(String srcFile, String nowFile);

    /**
     * 保存裁剪后的音频文件
     *
     * @param srcFile
     * @param nowFile
     * @param messageId
     * @return
     * @throws IOException
     */
    ServerResponse radioSave(String srcFile, String nowFile, String messageId) throws IOException;

    /**
     * 音频音量调整
     *
     * @param srcFile
     * @param type
     * @return
     */
    ServerResponse volumeTweak(String srcFile, Integer type);

    /**
     * 发布一条或多条消息
     *
     * @param messageIds
     * @return
     */
    void batchPublishMessage(String messageIds);

    /**
     * 下载文件到指定路径
     *
     * @param hostUrl 需要下载的源文件的网络路径
     * @param channelName
     * @return
     */
    String downloadFile(String hostUrl, String channelName) throws Exception;

    /**
     * 推荐发送
     *
     * @param content
     * @param transactionId
     * @param channelName
     * @throws IOException
     */
    void recommendSendMessage(String content, String transactionId, String channelName) throws IOException;

    /**
     * 获取消息数量
     *
     * @param fileType
     * @param source
     * @return
     */
    int totalMessage(String fileType, String source, String channelId);
}
