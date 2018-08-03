package com.redhat.springboot.vacationleave.employee.controller;

import com.redhat.springboot.vacationleave.employee.dto.EmployeeDto;
import com.redhat.springboot.vacationleave.employee.dto.SickRequestDto;
import com.redhat.springboot.vacationleave.employee.model.Employee;
import com.redhat.springboot.vacationleave.employee.service.EmployeeService;
import com.redhat.springboot.vacationleave.employee.service.SickRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/api/employees")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    SickRequestService sickRequestService;

    @Autowired
    ModelMapper modelMapper;

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeDto>> list(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        Page<Employee> employeePage = employeeService.list(new PageRequest(page, pageSize));
        if(employeePage.getContent() != null) {
            List<EmployeeDto> result = employeePage.getContent().stream()
                    .map(post -> convertToDto(post))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @PostMapping(value = "/sickRequest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SickRequestDto> sendSickRequest(@RequestBody EmployeeDto input) {
        SickRequestDto sickRequestDto = sickRequestService.sendRequest(input);
        return new ResponseEntity<>(sickRequestDto, HttpStatus.OK);

    }

    @ResponseBody
    @RequestMapping("/sickRequest/{ssn}")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SickRequestDto>> listBySSN(@PathVariable String ssn, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        List<SickRequestDto> result = sickRequestService.getRequestsBySSN(ssn, new PageRequest(page, pageSize));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private EmployeeDto convertToDto(Employee employee) {
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        return employeeDto;
    }
}
