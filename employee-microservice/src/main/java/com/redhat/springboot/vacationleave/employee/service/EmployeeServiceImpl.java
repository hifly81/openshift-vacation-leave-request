package com.redhat.springboot.vacationleave.employee.service;

import com.redhat.springboot.vacationleave.employee.model.Employee;
import com.redhat.springboot.vacationleave.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Page<Employee> list(PageRequest pageRequest) {
        return employeeRepository.findAll(pageRequest);
    }
}
