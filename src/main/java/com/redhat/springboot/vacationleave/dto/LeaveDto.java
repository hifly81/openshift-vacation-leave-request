package com.redhat.springboot.vacationleave.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.redhat.springboot.vacationleave.model.LeaveType;

@JsonIgnoreProperties(ignoreUnknown=true)
public class LeaveDto {

    private Integer id;

    private LeaveType typology;

    private Float entitled;

    private Float broughtForward;

    private Float broughtForwardExpired;

    private Float taken;

    private Float scheduled;

    private Float balance;

    @JsonIgnore
    private EmployeeDto employee;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LeaveType getTypology() {
        return typology;
    }

    public void setTypology(LeaveType typology) {
        this.typology = typology;
    }

    public Float getEntitled() {
        return entitled;
    }

    public void setEntitled(Float entitled) {
        this.entitled = entitled;
    }

    public Float getBroughtForward() {
        return broughtForward;
    }

    public void setBroughtForward(Float broughtForward) {
        this.broughtForward = broughtForward;
    }

    public Float getBroughtForwardExpired() {
        return broughtForwardExpired;
    }

    public void setBroughtForwardExpired(Float broughtForwardExpired) {
        this.broughtForwardExpired = broughtForwardExpired;
    }

    public Float getTaken() {
        return taken;
    }

    public void setTaken(Float taken) {
        this.taken = taken;
    }

    public Float getScheduled() {
        return scheduled;
    }

    public void setScheduled(Float scheduled) {
        this.scheduled = scheduled;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public EmployeeDto getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDto employee) {
        this.employee = employee;
    }
}
