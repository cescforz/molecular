package cn.cescforz.molecular.converter.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>Description: 日志获取ip地址</p>
 *
 * @author cecs
 * @version 1.00.00
 * @date Create in 2019/7/31 10:01
 */
@Slf4j
public class IpAddressConverter extends ClassicConverter {

    private static String ipAddress;
    static {
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("fetch localhost host address failed", e);
            ipAddress = "UNKNOWN";
        }
    }

    @Override
    public String convert(ILoggingEvent event) {
        return ipAddress;
    }
}
