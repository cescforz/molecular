package cn.cescforz.molecular.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: 自定义幂等注解</p>
 *
 * @author developer developer@midea.com.cn
 * @version 1.00.00
 * @date Create in 2019/8/23 13:46
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    // 注解自定义redis的key的一部分
    String key();

    // 过期时间
    long expire() default 60 * 3;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
