package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class CompanyServiceTest {
    private CompanyRepository mockedCompanyRepository ;
    private CompanyService  companyService;

    @BeforeEach
    void init() {
        mockedCompanyRepository = Mockito.mock(CompanyRepository.class);
        companyService = new CompanyService(mockedCompanyRepository);
    }
    private List<Company> generateCompanies() {
        List<Company> companies = new ArrayList<>();
        List<Employee> firstEmployees = new ArrayList<>();
        firstEmployees.add(new Employee(0, "alibaba1", 20, "male", 6000));
        firstEmployees.add(new Employee(1, "alibaba12", 19, "female", 7000));
        firstEmployees.add(new Employee(2, "alibaba3", 19, "male", 8000));
        companies.add(new Company(0,"alibaba",3,firstEmployees));
        List<Employee> secondEmployees = new ArrayList<>();
        firstEmployees.add(new Employee(3, "baidu1", 20, "male", 6000));
        firstEmployees.add(new Employee(4, "baidu2", 19, "female", 7000));
        firstEmployees.add(new Employee(5, "baidu3", 19, "male", 8000));
        companies.add(new Company(1,"baidu",3,secondEmployees));
        List<Employee> thirdEmployees = new ArrayList<>();
        thirdEmployees.add(new Employee(3, "baidu1", 20, "male", 6000));
        thirdEmployees.add(new Employee(4, "baidu2", 19, "female", 7000));
        thirdEmployees.add(new Employee(5, "baidu3", 19, "male", 8000));
        companies.add(new Company(1,"baidu",3,thirdEmployees));
        return companies;
    }

}