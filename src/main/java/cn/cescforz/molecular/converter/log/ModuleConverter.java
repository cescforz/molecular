package cn.cescforz.molecular.converter.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * <p>Description: 日志获取module</p>
 *
 * @author cecs
 * @version 1.00.00
 * @date Create in 2019/7/31 10:02
 */
public class ModuleConverter extends ClassicConverter {

    private static final int MAX_LENGTH = 20;

    @Override
    public String convert(ILoggingEvent event) {
        if (event.getLoggerName().length() > MAX_LENGTH) {
            return "";
        } else {
            return event.getLoggerName();
        }
    }
}
