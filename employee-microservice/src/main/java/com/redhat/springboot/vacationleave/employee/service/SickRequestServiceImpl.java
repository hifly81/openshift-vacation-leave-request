package com.redhat.springboot.vacationleave.employee.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.redhat.springboot.vacationleave.employee.dto.EmployeeDto;
import com.redhat.springboot.vacationleave.employee.dto.SickRequestDto;
import com.redhat.springboot.vacationleave.employee.tracing.SpanContextHystrixRequestVariable;
import com.redhat.springboot.vacationleave.employee.tracing.TracingRestHandlerInterceptor;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@Component
public class SickRequestServiceImpl implements SickRequestService {

    @Value("${sickrequests.service.url}")
    private String sickRequestsServiceUrl;

    @Autowired
    private Tracer tracer;

    @Override
    public SickRequestDto sendRequest(EmployeeDto employeeDto) {
        TracingRestHandlerInterceptor interceptor = new TracingRestHandlerInterceptor(tracer, tracer.activeSpan().context());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(interceptor);
        SickRequestDto sickRequestDto = new SickRequestDto();
        sickRequestDto.setEmployeeId(employeeDto.getSsn());
        sickRequestDto.setDateRequested(LocalDate.now());
        HttpEntity<SickRequestDto> entity = new HttpEntity<>(sickRequestDto);
        ResponseEntity<SickRequestDto> responseEntity =
                restTemplate.exchange(sickRequestsServiceUrl, HttpMethod.PUT, entity, SickRequestDto.class);
        return responseEntity.getBody();
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "getRequestsBySSNFallback",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "10"),
                    @HystrixProperty(name = "maxQueueSize", value = "5")
            })
    public List<SickRequestDto> getRequestsBySSN(String ssn, PageRequest pageRequest) {

        SpanContext spanContext = SpanContextHystrixRequestVariable.getInstance().get();
        TracingRestHandlerInterceptor interceptor = new TracingRestHandlerInterceptor(tracer, spanContext);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(interceptor);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(sickRequestsServiceUrl + "/")
                .queryParam("page", pageRequest.getPageNumber())
                .queryParam("pageSize", pageRequest.getPageSize());

        ResponseEntity<List<SickRequestDto>> responseEntity =
                    restTemplate.exchange(builder.buildAndExpand().toUri(),
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<SickRequestDto>>() {});

        return responseEntity.getBody();
    }

    public List<SickRequestDto> getRequestsBySSNFallback(String ssn, PageRequest pageRequest) {
        return null;
    }
}
