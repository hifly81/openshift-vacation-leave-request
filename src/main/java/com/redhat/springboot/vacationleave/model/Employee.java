package com.redhat.springboot.vacationleave.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String surname;

    private String ssn;

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private List<Leave> leaves = new ArrayList<>();

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

    public List<Leave> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<Leave> leaves) {
        this.leaves = leaves;
    }

    @Override
    public String toString() {
        String result = String.format("Employee[id=%d, name='%s, surname='%s', ssn='%s']", id, name, surname, ssn);
        if (leaves != null) {
            for(Leave leave : leaves) {
                result += String.format("Leave[id=%d, type='%s', balance='%.2f']%n", leave.getId(), leave.getTypology(), leave.getBalance());
            }
        }

        return result;
    }
}
