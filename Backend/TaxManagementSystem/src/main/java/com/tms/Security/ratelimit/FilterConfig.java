package com.tms.Security.ratelimit;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration
public class FilterConfig {

    @Bean
    public UserRateLimitFilter userRateLimitFilter() {
        return new UserRateLimitFilter();
    }

    @Bean
    public FilterRegistrationBean<UserRateLimitFilter> registerUserRateLimit(UserRateLimitFilter filter) {
        FilterRegistrationBean<UserRateLimitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/api/*");
        registration.setOrder(1);
        return registration;
    }
}



