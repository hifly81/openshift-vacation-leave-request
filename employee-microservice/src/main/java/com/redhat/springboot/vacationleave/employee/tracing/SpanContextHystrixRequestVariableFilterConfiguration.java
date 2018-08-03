package com.redhat.springboot.vacationleave.employee.tracing;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpanContextHystrixRequestVariableFilterConfiguration {

    @Bean
    public FilterRegistrationBean spanContextHystrixRequestVariableFilter() {

        SpanContextHystrixRequestVariableFilter filter = new SpanContextHystrixRequestVariableFilter();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(filter);
        filterRegistrationBean.addUrlPatterns("/*");
        // should run after security filters
        filterRegistrationBean.setOrder(5);
        filterRegistrationBean.setAsyncSupported(true);
        return filterRegistrationBean;
    }

}
