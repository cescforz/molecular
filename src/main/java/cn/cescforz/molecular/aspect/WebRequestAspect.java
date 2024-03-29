package cn.cescforz.molecular.aspect;

import cn.cescforz.commons.lang.bean.dto.ResponseDTO;
import cn.cescforz.commons.lang.enums.ResponseEnum;
import cn.cescforz.commons.lang.exception.CustomException;
import cn.cescforz.commons.lang.exception.CustomRtException;
import cn.cescforz.commons.lang.toolkit.util.StringTools;
import cn.cescforz.molecular.annotation.Idempotent;
import cn.cescforz.molecular.bean.domain.ApiLogDO;
import cn.cescforz.molecular.bean.domain.ErrorLogDO;
import cn.cescforz.molecular.biz.handler.RedisHandler;
import cn.cescforz.molecular.config.mq.producer.Producer;
import cn.cescforz.molecular.constant.ModuleConstants;
import cn.cescforz.molecular.constant.RedisConstants;
import cn.cescforz.molecular.constant.RocketMQConstants;
import cn.cescforz.molecular.toolkit.util.IllegalStrFilterUtils;
import cn.cescforz.molecular.toolkit.util.KeyUtils;
import cn.cescforz.molecular.toolkit.util.RedisUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>©2018 Cesc. All Rights Reserved.</p>
 * <p>Description: 网络请求切面类：幂等防重、接口调用异常处理</p>
 *
 * @author cesc
 * @version v1.0
 * @date Create in 2018-12-17 20:11
 */
@Aspect
@Component
@Slf4j
public class WebRequestAspect {

    @Value("${interface.request.timeout}")
    private int performanceBadValue;

    /**
     * 异常入库生产者
     */
    private Producer<ErrorLogDO> producer;
    /**
     * 接口调用信息入库生产者
     */
    private Producer<ApiLogDO> logProducer;


    private RedisHandler redisHandler;

    /**
     * 切点位置
     */
    private static final String POINT_CUT = ModuleConstants.API_POINT_CUT;

    @Pointcut(POINT_CUT)
    public void pointCut() {
        // to do nothing
    }

    @Around("pointCut()")
    public ResponseDTO handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        try {
            String methodName = String.valueOf(pjp.getSignature());
            String param = StringUtils.join(pjp.getArgs());
            log.info("执行Controller开始:{};参数:{}", methodName, param);
            // 处理入参特殊字符和sql注入攻击
            checkRequestParam(pjp);
            // 执行访问接口操作-->获取返回参数
            Object proceed = pjp.proceed();
            responseDTO.setRespData(proceed);
            log.info("执行Controller结束:{},返回值:{}", methodName, JSON.toJSONString(responseDTO, true));
            Long consumeTime = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
            log.info("耗时:{}(毫秒).", consumeTime);
            // 当接口请求时间大于3秒时，标记为异常调用时间，并记录入库
            if (consumeTime > performanceBadValue) {
                recordApiError(methodName, param, null, consumeTime);
            }
            recordApiLog(pjp, consumeTime);
        } catch (Exception throwable) {
            responseDTO = handlerException(pjp, throwable);
        }
        return responseDTO;
    }

    @Around(value = "@annotation(cn.cescforz.molecular.annotation.Idempotent)")
    public Object preventRepeat(ProceedingJoinPoint pjp) throws Throwable {
        // 获取当前方法信息
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        // 获取注解
        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        // 生成Key
        String key = RedisUtils.generateRedisKey(RedisConstants.IDEMPOTENT, "_", idempotent.key() + "_" + KeyUtils.generateApiKey(method, pjp.getArgs()));
        String requestId = RedisUtils.getRedisReqNumId();
        boolean flag = redisHandler.tryLock(key, requestId, idempotent.expire(), idempotent.timeUnit());
        if (!flag) {
            throw new CustomRtException(ResponseEnum.GATE_WAY_CHECK_SIGN_FAIL, "sorry!! Interface duplicates requests, violating idempotency.");
        }
        // 获取锁成功
        try {
            return pjp.proceed();
        } catch (Throwable e) {
            log.error("error:",e);
            throw e;
        } finally {
            // 释放锁
            redisHandler.releaseLock(key, requestId);
        }
    }

    /**
     * <p>Description: 处理异常并包装返回参数</p>
     * @param pjp
     * @param e
     * @return cn.cescforz.commons.lang.bean.dto.ResponseDTO<java.lang.Object>
     */
    private ResponseDTO<Object> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        String methodName = String.valueOf(pjp.getSignature());
        String param = StringUtils.join(pjp.getArgs());
        ResponseDTO<Object> responseDTO;
        if (e.getClass().isAssignableFrom(CustomException.class)) {
            CustomException ce = (CustomException) e;
            log.error("捕获到--<CustomException>--异常:{}", JSON.toJSONString(ce.getResponseEnum()), ce);
            responseDTO = new ResponseDTO<>(false, ce.getErrorCode(), ce.getMessage());
        } else if (e.getClass().isAssignableFrom(CustomRtException.class)) {
            CustomRtException cre = (CustomRtException) e;
            log.error("捕获到--<CustomRtException>--异常:{}", JSON.toJSONString(cre.getResponseEnum()), cre);
            responseDTO = new ResponseDTO<>(false, cre.getErrorCode(), cre.getMessage());
        } else if (e instanceof RuntimeException) {
            log.error(String.format("捕获到--<RuntimeException--> {方法:%s,参数:%s,异常:%s}", methodName, param, e.getMessage()), e);
            responseDTO = new ResponseDTO<>(false, null, e.getMessage());
        } else {
            log.error(String.format("捕获到--<Exception--> {方法:%s,参数:%s,异常:%s}", methodName, param, e.getMessage()), e);
            responseDTO = new ResponseDTO<>(false, null, e.getMessage());
        }
        recordApiError(methodName, param, e, null);
        return responseDTO;
    }

    /**
     * <p>Description: 异常或接口调用时间过长信息入库</p>
     *
     * @param methodName  调用方法
     * @param param       请求参数
     * @param e           异常
     * @param consumeTime 调用时间
     */
    private void recordApiError(String methodName, String param, Throwable e, Long consumeTime) {
        ErrorLogDO errorLog = new ErrorLogDO();
        errorLog.setInterfaceName(methodName);
        errorLog.setRequestParam(param);
        if (null != e) {
            StackTraceElement stackTraceElement = e.getStackTrace()[0];
            String errorInfo = String.format("%s,errorMassage:%s,errorLine:%d", e.toString(), stackTraceElement, stackTraceElement.getLineNumber());
            errorLog.setLogInfo(errorInfo);
        }
        if (null != consumeTime) {
            errorLog.setConsumeTime(consumeTime);
        }
        errorLog.setModuleType(ModuleConstants.FOO_MODULE_TYPE);
        producer.sendTxMsg(errorLog, RocketMQConstants.MARK_LOG, RocketMQConstants.HANDLE_EXCEPTIONS_TAG, null, null);
    }

    /**
     * <p>Description: 接口调用信息入库</p>
     *
     * @param joinPoint
     * @param consumeTime 接口耗时
     */
    private void recordApiLog(JoinPoint joinPoint, Long consumeTime) {
        // 获取HttpServletRequest
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != attributes) {
            HttpServletRequest request = attributes.getRequest();
            // 获取要记录的日志内容
            ApiLogDO apiLog = new ApiLogDO();
            apiLog.setRequestUrl(request.getRequestURL().toString());
            apiLog.setRequestUri(request.getRequestURI());
            apiLog.setQueryString(request.getQueryString());
            apiLog.setRemoteAddr(request.getRemoteAddr());
            apiLog.setRemoteHost(request.getRemoteHost());
            apiLog.setRemotePort(request.getRemotePort());
            apiLog.setLocalAddr(request.getLocalAddr());
            apiLog.setLocalName(request.getLocalName());
            apiLog.setMethod(request.getMethod());
            apiLog.setHeaders(getHeadersInfo(request));
            apiLog.setParameters(request.getParameterMap());
            apiLog.setClassMethod(StringTools.assembleStr(joinPoint.getSignature().getDeclaringTypeName(), ".", joinPoint.getSignature().getName()));
            apiLog.setArgs(StringUtils.join(joinPoint.getArgs()));
            apiLog.setConsumeTime(consumeTime);
            logProducer.sendMsg(apiLog, RocketMQConstants.MARK_LOG, RocketMQConstants.HANDLE_RECORDLOG_TAG, null);
        }
    }


    private void checkRequestParam(ProceedingJoinPoint pjp) {
        String methodName = String.valueOf(pjp.getSignature());
        String param = StringUtils.join(pjp.getArgs());
        if (!IllegalStrFilterUtils.sqlStrFilter(param)) {
            String warning = String.format("访问接口:%s,输入参数存在SQL注入风险！参数为:%s", methodName, param);
            log.warn(warning);
            throw new CustomRtException(ResponseEnum.ILLEGAL_ARGUMENT_EXCEPTION, warning);
        }
        if (IllegalStrFilterUtils.isIllegalStr(param)) {
            String warning = String.format("访问接口:%s,输入参数含有非法字符! 参数为:%s", methodName, param);
            log.warn(warning);
            throw new CustomRtException(ResponseEnum.ILLEGAL_ARGUMENT_EXCEPTION, warning);
        }
    }


    /**
     * <p>Description: 获取头信息</p>
     *
     * @param request
     * @return java.util.Map<java.lang.String, java.lang.String>
     */
    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = Maps.newHashMap();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    @Autowired
    public void setProducer(Producer<ErrorLogDO> producer) {
        this.producer = producer;
    }

    @Autowired
    public void setLogProducer(Producer<ApiLogDO> logProducer) {
        this.logProducer = logProducer;
    }

    @Autowired
    public void setRedisHandler(RedisHandler redisHandler) {
        this.redisHandler = redisHandler;
    }
}
