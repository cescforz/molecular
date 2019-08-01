package cn.cescforz.molecular.listener;

import cn.cescforz.molecular.constant.RocketMQConstants;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: RocketMQ事务消息监听实现类</p>
 * @author cesc
 * @version v1.0
 * @date Create in 2019-01-03 16:47
 */
@Slf4j
@Component
public class TransactionListenerImpl implements TransactionListener {

    /*
     * transaction的流程下，rocketmq会先发送一个consumer不可见的消息，然后在调用
     * TransactionListener这个接口中的executeLocalTransaction,中的方法执行事务，然后方法内部需要返回
     * 一个LocalTransactionState的枚举信息，分别为
     *     COMMIT_MESSAGE --> 提交到mq
     *     ROLLBACK_MESSAGE --> 回滚不提交到mq
     *     UNKNOW
     *
     */

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object o) {
        log.info(" ---> rocketMQ开始事物管理 <---");
        boolean flag = verify(msg);
        if(!flag){
            log.info("事务回滚了，不会发送");
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }

    /**
     * checkLocalTransaction是用作mq长时间没有收到producer的executeLocalTransaction响应的时候调用的
     */

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {


        return LocalTransactionState.COMMIT_MESSAGE;
    }

    private boolean verify(Message msg){
        String topic = msg.getTopic();
        String tags = msg.getTags();
        if(StringUtils.equals(topic, RocketMQConstants.MARK_LOG)){
            if (StringUtils.isNotBlank(tags)) {
                switch (tags) {
                    case RocketMQConstants.HANDLE_EXCEPTIONS_TAG:
                        return true;
                    default:
                        return false;
                }
            }
            return false;
        }
        return false;

    }


    /*
        RocketMQ事务消息执行步骤
        1.发送方向 MQ 服务端发送消息。
        2.MQ Server 将消息持久化成功之后，向发送方 ACK 确认消息已经发送成功，此时消息为半消息。
        3.发送方开始执行本地事务逻辑。
        4.发送方根据本地事务执行结果向 MQ Server 提交二次确认（Commit 或是 Rollback），
          MQ Server 收到 Commit 状态则将半消息标记为可投递，订阅方最终将收到该消息；
          MQ Server 收到 Rollback 状态则删除半消息，订阅方将不会接受该消息。
        5.在断网或者是应用重启的特殊情况下，上述步骤4提交的二次确认最终未到达 MQ Server，经过固定时间后 MQ Server 将对该消息发起消息回查。
        6.发送方收到消息回查后，需要检查对应消息的本地事务执行的最终结果。
        7.发送方根据检查得到的本地事务的最终状态再次提交二次确认，MQ Server 仍按照步骤4对半消息进行操作。
     */



}
