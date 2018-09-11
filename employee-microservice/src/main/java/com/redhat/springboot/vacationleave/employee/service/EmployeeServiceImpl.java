package com.redhat.springboot.vacationleave.employee.service;

import com.redhat.springboot.vacationleave.employee.dto.EmployeeDto;
import com.redhat.springboot.vacationleave.employee.dto.EmployeeExtraInfo;
import com.redhat.springboot.vacationleave.employee.model.Employee;
import com.redhat.springboot.vacationleave.employee.repository.EmployeeRepository;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.api.BasicCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RemoteCacheManager cacheManager;

    @Value("${employee.cache}")
    private String cacheName;

    @Override
    public Page<Employee> list(PageRequest pageRequest) {
        return employeeRepository.findAll(pageRequest);
    }

    @Override
    public EmployeeDto details(String ssn) {
        BasicCache<String, Object> cache = cacheManager.getCache(cacheName);
        Object cached = cache.get(ssn);
        if(cached == null) {
            EmployeeDto employeeDto = createMockEmployee(ssn);
            cache.put(ssn, employeeDto);
            return employeeDto;
        }
        else
            return (EmployeeDto) cached;
    }

    private EmployeeDto createMockEmployee(String ssn) {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setSsn(ssn);
        EmployeeExtraInfo extraInfo = new EmployeeExtraInfo();
        extraInfo.setCompanyId("The Magic Company");
        extraInfo.setMaritalStatus("Married");
        employeeDto.setExtraInfo(extraInfo);
        return employeeDto;

    }
}
