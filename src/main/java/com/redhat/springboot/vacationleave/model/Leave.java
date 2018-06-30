package com.redhat.springboot.vacationleave.model;

import javax.persistence.*;

@Entity
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private LeaveType typology;

    private Float entitled;

    @Column(name = "broughtforward")
    private Float broughtForward;

    @Column(name = "broughtforwardexpired")
    private Float broughtForwardExpired;

    private Float taken;

    private Float scheduled;

    private Float balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;


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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }


}
