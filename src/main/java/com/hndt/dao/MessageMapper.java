package com.hndt.dao;

import com.hndt.pojo.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    /**
     * 通过参数查询获取消息列表
     *
     * @param content
     * @param source
     * @param fileType
     * @return
     */
    List<Message> queryListByParam(@Param(value = "content") String content, @Param(value = "source") String source, @Param(value = "fileType") String fileType, @Param(value = "channelId") String channelId);

    /**
     * 批量删除消息
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);

    /**
     * 通过第三方id获取
     *
     * @param transactionId
     * @return
     */
    Message queryMessageByTransactionId(String transactionId);

    /**
     * 通过第三方id删除
     *
     * @param transactionId
     * @return
     */
    int deleteByTransactionId(Long transactionId);

    /**
     * 修改消息状态
     *
     * @param transactionId
     * @param status
     * @return
     */
    int updateStatusByTransactionId(@Param(value = "transactionId") String transactionId, @Param(value = "status") Integer status);

    /**
     * 批量发布消息
     *
     * @param ids
     * @return
     */
    int batchPublishMessage(List<Long> ids);

    /**
     * 计算消息总数
     *
     * @param fileType
     * @param source
     * @return
     */
    int totalMessage(@Param(value = "fileType") String fileType, @Param(value = "source") String source, @Param(value = "channelId") String channelId);

}