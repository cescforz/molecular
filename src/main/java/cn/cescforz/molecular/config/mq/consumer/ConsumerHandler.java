package cn.cescforz.molecular.config.mq.consumer;

import cn.cescforz.commons.lang.constant.SystemConstants;
import cn.cescforz.molecular.bean.domain.ApiLogDO;
import cn.cescforz.molecular.bean.domain.ErrorLogDO;
import cn.cescforz.molecular.bean.model.ConsumerEvent;
import cn.cescforz.molecular.biz.ApiLogCommand;
import cn.cescforz.molecular.biz.CommandHandler;
import cn.cescforz.molecular.biz.ErrorLogCommand;
import cn.cescforz.molecular.biz.RedisHanlder;
import cn.cescforz.molecular.constant.ModuleConstants;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: RocketMQ消费者处理类</p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-03 15:02
 */
@Slf4j
@Component
public class ConsumerHandler extends Consumer {

    private CommandHandler commandHandler;
    private ApiLogCommand apiLogCommand;
    private ErrorLogCommand errorLogCommand;
    private RedisHanlder redisHanlder;

    private static final Integer RECONSUME_END_TIME = 2;


    @Override
    @SuppressWarnings("unchecked")
    @EventListener(condition = "#event.msgs[0].topic=='MARK_LOG' && #event.msgs[0].tags=='HANDLE_EXCEPTION_TAG'")
    public void handleExceptionListener(ConsumerEvent event) {
        List<MessageExt> msgs = event.getMsgs();
        if (CollectionUtils.isNotEmpty(msgs)) {
            msgs.forEach(msg -> {
                // 自定义的唯一key
                String keys = msg.getKeys();
                if (null == redisHanlder.handleStr().get(keys)) {
                    // 消息id(不是唯一的)
                    String msgId = msg.getMsgId();
                    // 消息内容
                    String body = new String(msg.getBody(), SystemConstants.CHARSET_UTF_8);
                    // 重试次数
                    int reconsume = msg.getReconsumeTimes();
                    try {
                        ErrorLogDO errorLog = JSON.parseObject(body, ErrorLogDO.class);
                        Object o = commandHandler.dispatchSave(errorLogCommand, errorLog);
                        log.info("第{}次消费{}消息:{} -> 结果:{}", reconsume + 1, msgId, body, o);
                        redisHanlder.handleStr().set(keys, body, ModuleConstants.DEFAULT_EXPIRE, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.error("消费消息出错:", e);
                        // 重试3次就不在重试了,直接返回消费成功状态,并触发人工补偿机制。
                        if (reconsume == RECONSUME_END_TIME) {
                            redisHanlder.handleStr().set(keys, body, ModuleConstants.DEFAULT_EXPIRE, TimeUnit.SECONDS);
                        }
                    }
                }
            });
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @EventListener(condition = "#event.msgs[0].topic=='MARK_LOG' && #event.msgs[0].tags=='HANDLE_RECORDLOG_TAG'")
    public void handleLogListener(ConsumerEvent event) {
        List<MessageExt> msgs = event.getMsgs();
        if (CollectionUtils.isNotEmpty(msgs)) {
            msgs.forEach(msg -> {
                // 自定义的唯一key
                String keys = msg.getKeys();
                if (null == redisHanlder.handleStr().get(keys)) {
                    // 消息id(不是唯一的)
                    String msgId = msg.getMsgId();
                    // 消息内容
                    String body = new String(msg.getBody(), SystemConstants.CHARSET_UTF_8);
                    // 重试次数
                    int reconsume = msg.getReconsumeTimes();
                    try {
                        ApiLogDO apiLog = JSON.parseObject(body, ApiLogDO.class);
                        Object o = commandHandler.dispatchSave(apiLogCommand, apiLog);
                        log.info("第{}次消费{}消息:{} -> 结果:{}", reconsume + 1, msgId, body, o);
                        redisHanlder.handleStr().set(keys, body, ModuleConstants.DEFAULT_EXPIRE, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.error("消费消息出错:", e);
                        // 重试3次就不在重试了,直接返回消费成功状态,并触发人工补偿机制。
                        if (reconsume == RECONSUME_END_TIME) {
                            redisHanlder.handleStr().set(keys, body, ModuleConstants.DEFAULT_EXPIRE, TimeUnit.SECONDS);
                        }
                    }
                }
            });
        }
    }


    @Autowired
    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
    @Autowired
    public void setApiLogCommand(ApiLogCommand apiLogCommand) {
        this.apiLogCommand = apiLogCommand;
    }
    @Autowired
    public void setErrorLogCommand(ErrorLogCommand errorLogCommand) {
        this.errorLogCommand = errorLogCommand;
    }
    @Autowired
    public void setRedisHanlder(RedisHanlder redisHanlder) {
        this.redisHanlder = redisHanlder;
    }
}
