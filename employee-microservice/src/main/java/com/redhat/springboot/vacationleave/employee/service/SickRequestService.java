package com.redhat.springboot.vacationleave.employee.service;

import com.redhat.springboot.vacationleave.employee.dto.EmployeeDto;
import com.redhat.springboot.vacationleave.employee.dto.SickRequestDto;

public interface SickRequestService {

    SickRequestDto sendRequest(EmployeeDto employeeDto);
}
