package com.redhat.springboot.vacationleave.employee.hystrix;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HystrixRequestContextFilterConfiguration {

    @Bean
    public FilterRegistrationBean hystrixRequestContextFilter() {
        HystrixRequestContextFilter filter = new HystrixRequestContextFilter();
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(filter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.setAsyncSupported(true);
        return filterRegistrationBean;
    }

}
