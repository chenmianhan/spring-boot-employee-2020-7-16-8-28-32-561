package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.IllegalOperationException;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
import com.thoughtworks.springbootemployee.mapper.CompanyMapper;
import com.thoughtworks.springbootemployee.mapper.EmployeeMapper;
import com.thoughtworks.springbootemployee.mapper.response.CompanyResponse;
import com.thoughtworks.springbootemployee.mapper.response.EmployeeResponse;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    @Autowired
    private final CompanyMapper companyMapper;
    @Autowired
    private final EmployeeMapper employeeMapper;
    @Autowired
    private final EmployeeRepository employeeRepository;

    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper, EmployeeMapper employeeMapper, EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
    }

    public List<CompanyResponse> findAll() {
        List<Company> companies = companyRepository.findAll();
        List<CompanyResponse> companyResponses = new LinkedList<>();
        for (Company company : companies) {
            companyResponses.add(companyMapper.CompanyToCompanyResponse(company));
        }
        return companyResponses;
    }

    public CompanyResponse findById(int id) throws NoSuchDataException {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (!companyOptional.isPresent())
            throw new NoSuchDataException("No such id company");
        Company company = companyRepository.findById(id).get();
        return companyMapper.CompanyToCompanyResponse(company);
    }

    public List<EmployeeResponse> getEmployeesByCompanyId(int id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) return null;
        List<Employee> employees = company.getEmployees();
        List<EmployeeResponse> employeeResponses = new ArrayList<>();
        for (Employee employee : employees) {
            employeeResponses.add(employeeMapper.EmployeeToEmployeeResponse(employee));
        }
        return employeeResponses;
    }

    public Page<CompanyResponse> findAll(int page, int pageSize) {
        Page<Company> companyPage = companyRepository.findAll(PageRequest.of(page - 1, pageSize));
        List<Company> companies = companyPage.getContent();
        List<CompanyResponse> companyResponses = new LinkedList<>();
        for (Company company : companies) {
            companyResponses.add(companyMapper.CompanyToCompanyResponse(company));
        }
        return new PageImpl<>(companyResponses);
    }

    public CompanyResponse save(Company newCompany) {
        Company company = companyRepository.save(newCompany);
        List<Employee> employees = company.getEmployees();
        if (employees != null)
            for (Employee employee : employees) {
                employee.setCompanyId(company.getId());
                employeeRepository.save(employee);
            }
        return companyMapper.CompanyToCompanyResponse(company);
    }

    public CompanyResponse updateEmployee(int id, Company newCompany) throws IllegalOperationException, NoSuchDataException {
        if (id != newCompany.getId()) throw new IllegalOperationException("id is not corresponding");
        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) throw new NoSuchDataException("No such id company");
        if (newCompany.getCompanyName() != null) company.setCompanyName(newCompany.getCompanyName());
        companyRepository.save(company);
        return companyMapper.CompanyToCompanyResponse(company);
    }

    public void deleteById(int id) throws NoSuchDataException {

        Optional<Company> companyOptional = companyRepository.findById(id);
        if (!companyOptional.isPresent())
            throw new NoSuchDataException("No such id company");
        //TODO 删除employees
        companyRepository.deleteById(id);

    }
}

