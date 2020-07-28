package com.thoughtworks.springbootemployee.model;

public class Employee {
    private int id;
    private String gender;

    public Employee() {
    }

    public Employee(int id, String gender) {
        this.id = id;
        this.gender = gender;
    }

    public Employee(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
