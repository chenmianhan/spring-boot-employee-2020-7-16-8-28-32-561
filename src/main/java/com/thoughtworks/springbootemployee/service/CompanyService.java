package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository=companyRepository;
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company findById(int id) {
        return companyRepository.findById(id).orElse(null);
    }

    public List<Employee> getEmployeesByCompanyId(int id) {
        Company company = companyRepository.findById(id).orElse(null);
        if(company==null)return null;
        return company.getEmployees();
    }

    public Page<Company> findAll(int page, int pageSize) {
        return companyRepository.findAll(PageRequest.of(page,pageSize));
    }

    public Company save(Company newCompany) {
        return companyRepository.save(newCompany);
    }
}

