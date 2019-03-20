package com.hndt.controller;

import com.hndt.common.Const;
import com.hndt.common.ServerResponse;
import com.hndt.enums.MessageTypeEnum;
import com.hndt.enums.ResultEnum;
import com.hndt.pojo.Manager;
import com.hndt.pojo.Message;
import com.hndt.service.MessageService;
import com.hndt.util.JsonUtil;
import com.hndt.vo.InitVo;
import com.hndt.vo.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 消息 Controller
 *
 * @author Hystar
 * @date 2018/1/10
 */
@Controller
@RequestMapping(value = "/message")
@Slf4j
public class MessageController {

    @Resource
    private MessageService messageService;

    @RequestMapping(value = "/init.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse init(HttpServletRequest request,
                               @RequestParam(value = "source", required = false) String source) {
        Manager manager = (Manager) request.getSession().getAttribute(Const.LITERAL_MANAGER);

        // 设定频率Id为120都转成24
        if (manager.getChannelId() == Long.parseLong("120")) {
            manager.setChannelId(24L);
        }

        String channelName = (String) request.getSession().getAttribute("channelName");

        InitVo initVo = new InitVo();
        initVo.setChannelName(channelName);
        initVo.setLoginIcon(manager.getIcon());
        initVo.setLoginName(manager.getName());
        initVo.setTotalMessage(messageService.totalMessage(null, source, String.valueOf(manager.getChannelId())));
        initVo.setTotalVoiceMessage(messageService.totalMessage(MessageTypeEnum.VOICE.getValue(), source, String.valueOf(manager.getChannelId())));
        initVo.setTotalTextMessage(messageService.totalMessage(MessageTypeEnum.TEXT.getValue(), source, String.valueOf(manager.getChannelId())));
        initVo.setTotalPicMessage(messageService.totalMessage(MessageTypeEnum.PICTURE.getValue(), source, String.valueOf(manager.getChannelId())));

        return ServerResponse.createBySuccess(initVo);
    }

    /**
     * 添加一条消息到本地
     *
     * @param messageVo
     * @return
     */
    @RequestMapping(value = "/insert.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse insertMessage(MessageVo messageVo,
                                        HttpServletRequest request) {
        String channelName = (String) request.getSession().getAttribute("channelName");

        try {
            messageService.save(messageVo, channelName);
            return ServerResponse.createBySuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("消息添加失败");
        }
    }

    /**
     * 获取当前登录用户的频率ID
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/get_channel_id.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getChannelId(HttpServletRequest request) {
        Manager manager = (Manager) request.getSession().getAttribute(Const.LITERAL_MANAGER);

        // 设定频率Id为120都转成24
        if (manager.getChannelId() == Long.parseLong("120")) {
            manager.setChannelId(24L);
        }

        return ServerResponse.createBySuccess(manager.getChannelId());
    }

    /**
     * 通过请求参数获取分页信息
     *
     * @param pageNum
     * @param pageSize
     * @param content
     * @return
     */
    @RequestMapping(value = "/list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse list(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                               @RequestParam(value = "content", required = false) String content,
                               @RequestParam(value = "source", required = false) String source,
                               @RequestParam(value = "fileType", required = false) String fileType,
                               HttpServletRequest request) {

        Manager manager = (Manager) request.getSession().getAttribute(Const.LITERAL_MANAGER);

        // 设定频率Id为120都转成24
        if (manager.getChannelId() == Long.parseLong("120")) {
            manager.setChannelId(24L);
        }

        String channelId = String.valueOf(manager.getChannelId());
        log.info("【获取分页信息】登录用户信息={}", JsonUtil.toJson(manager));
        log.info("【获取分页信息】获取频率id={}", channelId);

        return messageService.list(content, source, fileType, pageNum, pageSize, channelId);
    }

    /**
     * 删除一条消息
     *
     * @param transactionId
     * @return
     */
    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse delete(@RequestParam(value = "transactionId") String transactionId) {
        try {
            messageService.delete(transactionId);
            return ServerResponse.createBySuccessMessage("消息刪除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("消息刪除失败");
        }
    }

    /**
     * 批量删除消息
     *
     * @param messageIds
     * @return
     */
    @RequestMapping(value = "/batch_delete.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse batchDelete(@RequestParam(value = "messageIds") String messageIds) {
        try {
            messageService.batchDelete(messageIds);
            return ServerResponse.createBySuccessMessage("消息刪除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("消息刪除失敗");
        }
    }

    /**
     * 获取消息详情
     *
     * @param messageId
     * @return
     */
    @RequestMapping(value = "/detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse detail(@RequestParam(value = "messageId") String messageId) {
        try {
            Message message = messageService.detail(messageId);
            return ServerResponse.createBySuccess(message);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("消息获取失败");
        }
    }

    /**
     * 发布一条或多条消息
     *
     * @param messageIds
     * @return
     */
    @RequestMapping(value = "/batch_publish_message.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse batchPublishMessage(@RequestParam(value = "messageIds") String messageIds) {
        try {
            messageService.batchPublishMessage(messageIds);
            return ServerResponse.createBySuccessMessage("消息发布成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("消息发布失败");
        }
    }

    /**
     * 音频的裁剪
     *
     * @param startTime
     * @param duration
     * @param srcFile
     * @return
     */
    @RequestMapping(value = "/radio_tailor.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse radioTailor(@RequestParam(value = "startTime") Integer startTime,
                                      @RequestParam(value = "duration") Integer duration,
                                      @RequestParam(value = "srcFile") String srcFile) {
        return messageService.radioTailor(startTime, duration, srcFile);
    }

    /**
     * 音频的撤回
     *
     * @param srcFile
     * @return
     */
    @RequestMapping(value = "/radio_repeal.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse radioRepeal(@RequestParam(value = "srcFile") String srcFile,
                                      @RequestParam(value = "nowFile") String nowFile) {
        return messageService.radioRepeal(srcFile, nowFile);
    }

    /**
     * 音频的保存
     *
     * @param srcFile
     * @return
     */
    @RequestMapping(value = "/radio_save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse radioSave(@RequestParam(value = "srcFile") String srcFile,
                                    @RequestParam(value = "nowFile") String nowFile,
                                    @RequestParam(value = "messageId") String messageId) throws IOException {
        return messageService.radioSave(srcFile, nowFile, messageId);
    }

    /**
     * 音频音量调整
     *
     * @param srcFile
     * @param type
     */
    @RequestMapping(value = "/volume_tweak.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse volumeTweak(@RequestParam(value = "srcFile") String srcFile,
                                      @RequestParam(value = "type") Integer type) throws IOException {
        return messageService.volumeTweak(srcFile, type);
    }

//    /**
//     * 文件下载
//     *
//     * @param url     文件流路径
//     * @param request
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping(value = "/download_voice.do", method = RequestMethod.POST)
//    @ResponseBody
//    public ServerResponse download(@RequestParam String url,
//                                   HttpServletRequest request) {
//        String channelName = (String) request.getSession().getAttribute("channelName");
//        return messageService.downloadFile(url, channelName);
//    }

    /**
     * 推荐发送
     *
     * @param content
     * @param transactionId
     * @return
     */
    @RequestMapping(value = "/recommend_send_message.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse recommendSendMessage(@RequestParam(value = "content") String content,
                                               @RequestParam(value = "transactionId") String transactionId,
                                               HttpServletRequest request) {

        String channelName = (String) request.getSession().getAttribute("channelName");
        if (StringUtils.isEmpty(channelName)) {
            return ServerResponse.createByErrorCodeMessage(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getValue());
        }

        try {
            messageService.recommendSendMessage(content, transactionId, channelName);
            return ServerResponse.createBySuccessMessage("消息推荐成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("消息推荐失败");
        }
    }

}
