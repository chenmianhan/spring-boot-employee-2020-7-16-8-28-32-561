package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.IllegalOperationException;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository=companyRepository;
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company findById(int id) throws NoSuchDataException {
        //TODO
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (!companyOptional.isPresent())
            throw new NoSuchDataException("No such id company");
        return companyRepository.findById(id).orElse(null);
    }

    public List<Employee> getEmployeesByCompanyId(int id) {
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

    public Company updateEmployee(int id, Company newCompany) throws IllegalOperationException {
        //TODO
        if (id != newCompany.getId()) throw new IllegalOperationException("id is not correspond");
        Company company = companyRepository.findById(id).orElse(null);
        if (company != null) {

            if (newCompany.getCompanyName() != null) company.setCompanyName(newCompany.getCompanyName());
            if (newCompany.getEmployeeNumber() != null) company.setEmployeeNumber(newCompany.getEmployeeNumber());
            companyRepository.save(company);
        }
        return company;
    }

    public void deleteById(int id) {
        //TODO
        companyRepository.deleteById(id);

    }
}

