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
    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository=companyRepository;
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company findById(int id) {
        //TODO
        return companyRepository.findById(id).orElse(null);
    }

    public List<Employee> getEmployeesByCompanyId(int id) {
        //TODO
        Company company = companyRepository.findById(id).orElse(null);
        if(company==null)return null;
        return company.getEmployees();
    }

    public Page<Company> findAll(int page, int pageSize) {
        return companyRepository.findAll(PageRequest.of(page - 1, pageSize));
    }

    public Company save(Company newCompany) {
        return companyRepository.save(newCompany);
    }

    public Company updateEmployee(int id, Company targetCompany) {
        //TODO
        Company company=companyRepository.findById(id).orElse(null);
        if(company!=null){
         if(targetCompany.getCompanyName()!=null)company.setCompanyName(targetCompany.getCompanyName());
         if(targetCompany.getEmployeeNumber()!=null)company.setEmployeeNumber(targetCompany.getEmployeeNumber());
        companyRepository.save(company);
        }
        return company;
    }

    public void deleteById(int id) {
        //TODO
        companyRepository.deleteById(id);

    }
}

