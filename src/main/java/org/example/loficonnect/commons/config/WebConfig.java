package org.example.loficonnect.commons.config;

import org.example.loficonnect.interceptor.AppKeyInterceptor;
import org.example.loficonnect.usage.interceptor.ApiUsageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AppKeyInterceptor appKeyInterceptor;
    private final ApiUsageInterceptor apiUsageInterceptor;

    public WebConfig(AppKeyInterceptor appKeyInterceptor,
                     ApiUsageInterceptor apiUsageInterceptor) {
        this.appKeyInterceptor = appKeyInterceptor;
        this.apiUsageInterceptor = apiUsageInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. AppKeyInterceptor — validates the app key and checks the linked subscription.
        //    Populates AppKeyContext on success.
        registry.addInterceptor(appKeyInterceptor).addPathPatterns("/api/v1/ghl/**");
        // 2. ApiUsageInterceptor — reads AppKeyContext in afterCompletion and saves the log
        //    asynchronously. Registered second so it runs AFTER AppKeyInterceptor populates
        //    the context (interceptors execute in registration order for preHandle, and LIFO
        //    for afterCompletion — so afterCompletion here runs before AppKeyInterceptor clears).
        registry.addInterceptor(apiUsageInterceptor).addPathPatterns("/api/v1/ghl/**");
    }

    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
}
