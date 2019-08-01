package cn.cescforz.molecular.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>©2019 Cesc. All Rights Reserved.</p>
 * <p>Description: 基础控制器</p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2019-03-08 10:27
 */
@Controller
public class BaseController {

    protected HttpServletRequest getRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return null != attr ? attr.getRequest() : null;
    }

    protected HttpServletResponse getResponse() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return null != attr ? attr.getResponse() : null;
    }

    protected String redirect(String url) {
        return String.format("redirect:%s",url);
    }

    protected String forward(String url) {
        return String.format("forward:%s",url);
    }

    protected HttpSession getSession() {
        HttpServletRequest request = getRequest();
        return null != request ? request.getSession() : null;
    }

    protected Object getSessionObj(String key) {
        HttpSession session = getSession();
        return null != session ? session.getAttribute(key) : null;
    }

    protected void putSessionObj(String key, Object obj) {
        HttpSession session = getSession();
        if (null != session) {
            session.setAttribute(key, obj);
        }
    }

    protected void removeSessionObj(String key) {
        HttpSession session = getSession();
        if (null != session) {
            session.removeAttribute(key);
        }
    }



    protected String getIpAddr(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }



}
