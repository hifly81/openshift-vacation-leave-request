package com.redhat.springboot.vacationleave.employee.repository;

import com.redhat.springboot.vacationleave.employee.model.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Integer> {
}
