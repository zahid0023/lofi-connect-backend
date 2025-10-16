package org.example.loficonnect.config;

import org.example.loficonnect.interceptor.AppKeyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AppKeyInterceptor appKeyInterceptor;

    public WebConfig(AppKeyInterceptor appKeyInterceptor) {
        this.appKeyInterceptor = appKeyInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(appKeyInterceptor).addPathPatterns("/api/v1/ghl/**");
    }
}
