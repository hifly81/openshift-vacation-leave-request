package com.redhat.springboot.vacationleave.service;

import com.redhat.springboot.vacationleave.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface EmployeeService {

    Page<Employee> list(PageRequest pageRequest);


}
