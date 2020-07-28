package com.thoughtworks.springbootemployee.model;

import java.util.List;

public class Company {
    private int id;
    private List<Employee> employees;

    public Company() {
    }

    public Company(List<Employee> employees) {
        this.employees = employees;
    }

    public Company(int id) {
        this.id = id;
    }

    public Company(Company company) {
        this.id = company.getId();
        this.employees = company.getEmployees();
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
}
