package com.redhat.springboot.vacationleave.employee.service;

import com.redhat.springboot.vacationleave.employee.dto.EmployeeDto;
import com.redhat.springboot.vacationleave.employee.dto.SickRequestDto;
import com.redhat.springboot.vacationleave.employee.tracing.TracingRestHandlerInterceptor;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

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
        SickRequestDto sickRequestDto = new SickRequestDto();
        sickRequestDto.setEmployeeId(employeeDto.getSsn());
        sickRequestDto.setDateRequested(LocalDate.now());
        HttpEntity<SickRequestDto> entity = new HttpEntity<>(sickRequestDto);
        ResponseEntity<SickRequestDto> responseEntity =
                restTemplate.exchange(sickRequestsServiceUrl, HttpMethod.PUT, entity, SickRequestDto.class);
        return responseEntity.getBody();
    }
}
