package com.redhat.springboot.vacationleave.repository;

import com.redhat.springboot.vacationleave.model.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Integer> {
}
