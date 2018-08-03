package com.redhat.springboot.vacationleave.employee.service;

import com.redhat.springboot.vacationleave.employee.dto.EmployeeDto;
import com.redhat.springboot.vacationleave.employee.dto.SickRequestDto;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface SickRequestService {

    SickRequestDto sendRequest(EmployeeDto employeeDto);

    List<SickRequestDto> getRequestsBySSN(String ssn, PageRequest pageRequest);
}
