package com.redhat.springboot.vacationleave.employee.service;

import com.redhat.springboot.vacationleave.employee.dto.EmployeeDto;
import com.redhat.springboot.vacationleave.employee.dto.SickRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Component
public class SickRequestServiceImpl implements SickRequestService {

    @Value("${sickrequests.service.url}")
    private String sickRequestsServiceUrl;

    @Override
    public SickRequestDto sendRequest(EmployeeDto employeeDto) {
        RestTemplate restTemplate = new RestTemplate();
        SickRequestDto sickRequestDto = new SickRequestDto();
        sickRequestDto.setEmployeeId(employeeDto.getId());
        sickRequestDto.setDateRequested(LocalDate.now());
        HttpEntity<SickRequestDto> entity = new HttpEntity<>(sickRequestDto);
        System.out.println("ENTRITTTTT:::::" + entity.getBody().getDateRequested());
        ResponseEntity<SickRequestDto> responseEntity =
                restTemplate.exchange(sickRequestsServiceUrl, HttpMethod.PUT, entity, SickRequestDto.class);
        return responseEntity.getBody();
    }
}
