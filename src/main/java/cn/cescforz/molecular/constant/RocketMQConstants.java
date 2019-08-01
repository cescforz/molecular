package cn.cescforz.molecular.constant;


/**
 * <p>©2018 Cesc. All Rights Reserved.</p>
 * <p>Description: </p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2018-12-27 16:59
 */
public final class RocketMQConstants {

    private RocketMQConstants(){throw new AssertionError();}


    /*
     * controller层异常入库的topic与tag配置
     * topic: 一类业务可以归为一个topic，比如所有的发邮件功能
     * tag: 某类业务下的细分，比如发送邮件业务下的发送注册邮件可以使用一个tag，发送忘记密码邮件可以再使用一个tag
     */

    /**catch_bar的topic*/
    public static final String MARK_LOG = "MARK_LOG";

    /**处理异常的tag*/
    public static final String HANDLE_EXCEPTIONS_TAG = "HANDLE_EXCEPTION_TAG";
    public static final String HANDLE_RECORDLOG_TAG = "HANDLE_RECORDLOG_TAG";

}
