package cn.cescforz.molecular.toolkit.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Description: RocketMQ枚举类</p>
 *
 * @author developer developer@midea.com.cn
 * @version 1.00.00
 * @date Create in 2019/8/1 10:01
 */
@Getter
@AllArgsConstructor
public enum RocketMqEnum {

    /** 记录日志 */
    HANDLE_RECORD_LOG("MO_MARK_LOG","HANDLE_RECORD_LOG_TAG"),
    HANDLE_EXCEPTIONS("MO_MARK_LOG","HANDLE_EXCEPTION_TAG");

    private String topic;
    private String tag;
}
