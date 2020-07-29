package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @GetMapping(params = {"page", "pageSize"})
    public List<Company> getCompaniesByPageAndPageSize(int page, int pageSize) {
        List<Company> companies = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(4, "alibaba1", 20, "male", 6000));
        employees.add(new Employee(11, "tengxun2", 19, "female", 7000));
        employees.add(new Employee(6, "alibaba3", 19, "male", 8000));
        for (int i = 0; i < pageSize; i++) {
            companies.add(new Company(i, "alibaba", 200, employees));
        }
        return companies;
    }

    @GetMapping
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(4, "alibaba1", 20, "male", 6000));
        employees.add(new Employee(11, "tengxun2", 19, "female", 7000));
        employees.add(new Employee(6, "alibaba3", 19, "male", 8000));
        companies.add(new Company(1, "alibaba", 200, employees));
        return companies;
    }

    @GetMapping("/{id}")
    public Company getCompanyById(@PathVariable int id) {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(4, "alibaba1", 20, "male", 6000));
        employees.add(new Employee(11, "tengxun2", 19, "female", 7000));
        employees.add(new Employee(6, "alibaba3", 19, "male", 8000));
        return new Company(id, "alibaba", 200, employees);
    }

    @GetMapping("/{id}/employees")
    public List<Employee> getEmployeesByCompanyId(@PathVariable int id) {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(4, "alibaba1", 20, "male", 6000));
        employees.add(new Employee(11, "tengxun2", 19, "female", 7000));
        employees.add(new Employee(6, "alibaba3", 19, "male", 8000));
        Company company = new Company(id);
        company.setEmployees(employees);
        return company.getEmployees();
    }

    @PostMapping
    public Company addCompany(@RequestBody Company company) {
        return new Company(company.getId(), company.getCompanyName(), company.getEmployeeNumber(), company.getEmployees());
    }

    @PutMapping("/{id}")
    public Company updateCompany(@PathVariable int id, @RequestBody Company newCompany) {
        Company oldCompany = new Company(id);
        oldCompany.setEmployees(newCompany.getEmployees());
        oldCompany.setCompanyName(newCompany.getCompanyName());
        oldCompany.setEmployeeNumber(newCompany.getEmployeeNumber());
        return oldCompany;
    }

    @DeleteMapping("/{id}")
    public Company deleteAllEmployeesByCompanyId(@PathVariable int id) {
        Company company = new Company(id);
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(4, "alibaba1", 20, "male", 6000));
        employees.add(new Employee(11, "tengxun2", 19, "female", 7000));
        employees.add(new Employee(6, "alibaba3", 19, "male", 8000));
        company.setEmployees(employees);
        company.setEmployees(new ArrayList<>());
        return company;
    }
}
