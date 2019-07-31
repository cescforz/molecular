package cn.cescforz.molecular.config;

import cn.cescforz.commons.lang.version.CustomRequestMappingHandlerMapping;
import cn.cescforz.molecular.interceptor.ApiInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * <p>Description: </p>
 *
 * @author cesc
 * @version 1.00.00
 * @date Create in 2019/7/31 11:34
 */
public class WebConfig extends WebMvcConfigurationSupport {


    /**
     * 接口认证拦截器
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(new ApiInterceptor());
    }

    @Bean
    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new CustomRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setInterceptors(getInterceptors());
        return handlerMapping;
    }
}
