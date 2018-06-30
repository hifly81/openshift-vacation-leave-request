package com.redhat.springboot.vacationleave.employee.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class EmployeeDto {

    private Integer id;

    private String name;

    private String surname;

    private String ssn;

    private List<LeaveDto> leaves = new ArrayList<>();


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public List<LeaveDto> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<LeaveDto> leaves) {
        this.leaves = leaves;
    }
}
