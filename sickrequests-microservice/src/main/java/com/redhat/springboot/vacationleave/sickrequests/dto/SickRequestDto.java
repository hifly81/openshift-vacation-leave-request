package com.redhat.springboot.vacationleave.sickrequests.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redhat.springboot.vacationleave.sickrequests.model.SickRequestStatus;

import java.time.LocalDate;

public class SickRequestDto {

    @JsonIgnore
    private Integer id;

    private String employeeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateRequested;

    private SickRequestStatus sickRequestStatus;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(LocalDate dateRequested) {
        this.dateRequested = dateRequested;
    }

    public SickRequestStatus getSickRequestStatus() {
        return sickRequestStatus;
    }

    public void setSickRequestStatus(SickRequestStatus sickRequestStatus) {
        this.sickRequestStatus = sickRequestStatus;
    }
}
