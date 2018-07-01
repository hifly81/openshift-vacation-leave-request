package com.redhat.springboot.vacationleave.sickrequests.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sickrequest")
public class SickRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "employeeid")
    private String employeeId;

    @Column(name = "daterequested")
    private LocalDate dateRequested;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, columnDefinition = "varchar(32) default 'PENDING'")
    private SickRequestStatus sickRequestStatus = SickRequestStatus.PENDING;


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
