package com.thoughtworks.springbootemployee.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String companyName;
    private int employeeNumber;
    @OneToMany
    private List<Employee> employees;


    public Company(int id) {
        this.id = id;
    }

    public Company(Integer id, String companyName, int employeeNumber, List<Employee> employees) {
        this.id = id;
        this.companyName = companyName;
        this.employeeNumber = employeeNumber;
        this.employees = employees;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
}
