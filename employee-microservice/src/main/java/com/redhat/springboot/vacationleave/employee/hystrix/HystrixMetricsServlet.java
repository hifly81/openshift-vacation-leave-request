package com.redhat.springboot.vacationleave.employee.hystrix;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

@Configuration
public class HystrixMetricsServlet {
    
    @Bean
    public ServletRegistrationBean hystrixMetricsServletRegistrationBean() {
        ServletRegistrationBean srb = new ServletRegistrationBean();
        srb.setServlet(new HystrixMetricsStreamServlet());
        srb.addUrlMappings("/hystrix.stream");
        srb.setEnabled(true);
        return srb;
    }

}
