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
        for (int i = 0; i < pageSize; i++) {
            companies.add(new Company(i));
        }
        return companies;
    }

    @GetMapping
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        companies.add(new Company(1));
        companies.add(new Company(2));
        return companies;
    }

    @GetMapping("/{id}")
    public Company getCompanyById(@PathVariable int id) {
        return new Company(id);
    }

    @GetMapping("/{id}/employees")
    public List<Employee> getEmployeesByCompanyId(@PathVariable int id) {
        Company company = new Company(id);
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1));
        employees.add(new Employee(2));
        company.setEmployees(employees);
        return company.getEmployees();
    }

    @PostMapping
    public Company addCompany(@RequestBody Company company) {
        return company;
    }

    @PutMapping("/{id}")
    public Company updateCompany(@PathVariable int id, @RequestBody Company newCompany) {
        Company oldCompany = new Company(id);
        oldCompany.setId(newCompany.getId());
        oldCompany.setEmployees(newCompany.getEmployees());
        return oldCompany;
    }

    @DeleteMapping("/{id}")
    public Company deleteAllEmployeesByCompanyId(@PathVariable int id) {
        Company company = new Company(id);
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1));
        employees.add(new Employee(2));
        employees.add(new Employee(3));
        company.setEmployees(employees);
        company.setEmployees(new ArrayList<>());
        return company;
    }
}
