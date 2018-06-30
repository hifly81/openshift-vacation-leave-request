package com.redhat.springboot.vacationleave.employee.service;

import com.redhat.springboot.vacationleave.employee.dto.EmployeeDto;

public interface SickRequestService {

    void sendRequest(EmployeeDto employeeDto);
}
