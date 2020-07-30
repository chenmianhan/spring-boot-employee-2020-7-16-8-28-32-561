package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    @Autowired
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company addCompany(@RequestBody Company company) {
        return companyService.save(company);
    }
    @GetMapping(params = {"page", "pageSize"})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Page<Company> getCompaniesByPageAndPageSize(int page, int pageSize) {

        return companyService.findAll(page-1,pageSize);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Company> getAllCompanies() {
        return companyService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Company getCompanyById(@PathVariable int id) {
        return companyService.findById(id);
    }

    @GetMapping("/{id}/employees")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Employee> getEmployeesByCompanyId(@PathVariable int id) {
        return companyService.getEmployeesByCompanyId(id);
    }



    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Company updateCompany(@PathVariable int id, @RequestBody Company newCompany) {

        return companyService.updateEmployee(id,newCompany);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAllEmployeesByCompanyId(@PathVariable int id) {

        companyService.deleteById(id);
    }
}
