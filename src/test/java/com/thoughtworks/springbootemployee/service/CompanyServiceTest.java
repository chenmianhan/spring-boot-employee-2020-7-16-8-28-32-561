package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class CompanyServiceTest {
    private CompanyRepository mockedCompanyRepository;
    private CompanyService companyService;

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
        companies.add(new Company(0, "alibaba", 3, firstEmployees));
        List<Employee> secondEmployees = new ArrayList<>();
        secondEmployees.add(new Employee(3, "baidu1", 20, "male", 6000));
        secondEmployees.add(new Employee(4, "baidu2", 19, "female", 7000));
        secondEmployees.add(new Employee(5, "baidu3", 19, "male", 8000));
        companies.add(new Company(1, "baidu", 3, secondEmployees));
        List<Employee> thirdEmployees = new ArrayList<>();
        thirdEmployees.add(new Employee(6, "tencent1", 20, "male", 6000));
        thirdEmployees.add(new Employee(7, "tencent2", 19, "female", 7000));
        thirdEmployees.add(new Employee(8, "tencent3", 19, "male", 8000));
        companies.add(new Company(2, "tencent", 3, thirdEmployees));
        return companies;
    }

    @Test
    void should_return_3_companies_when_get_all_companies_given_no_parameter() {
        //given
        List<Company> companies = new ArrayList<>();
        List<Employee> firstEmployees = new ArrayList<>();
        firstEmployees.add(new Employee(0, "alibaba1", 20, "male", 6000));
        companies.add(new Company(0, "alibaba", 3, firstEmployees));

        List<Employee> secondEmployees = new ArrayList<>();
        secondEmployees.add(new Employee(4, "baidu1", 20, "male", 6000));
        companies.add(new Company(1, "baidu", 3, secondEmployees));

        List<Employee> thirdEmployees = new ArrayList<>();
        thirdEmployees.add(new Employee(6, "tencent1", 20, "male", 6000));
        companies.add(new Company(2, "tencent", 3, thirdEmployees));
        when(mockedCompanyRepository.findAll()).thenReturn(generateCompanies());

        //when
        List<Company> foundCompanies = companyService.findAll();

        //then
        assertEquals(3, companies.size());
    }

    @Test
    void should_return_id_1_company_when_get_company_by_id_given_1() {
        //given
        List<Employee> secondEmployees = new ArrayList<>();
        secondEmployees.add(new Employee(3, "baidu1", 20, "male", 6000));
        Company mockedCompany = new Company(1, "baidu", 1, secondEmployees);
        int id = 1;
        given(mockedCompanyRepository.findById(id)).willReturn(java.util.Optional.of(mockedCompany));

        //when
        Company company = companyService.findById(id);

        //then
        assertEquals("baidu", company.getCompanyName());
        assertEquals(1, company.getId());

    }

    @Test
    void should_return_baidu_employees_when_get_employee_by_id_given_1() {
        //given
        int id = 1;
        when(mockedCompanyRepository.findById(id)).thenReturn(generateCompanies().stream().filter(company -> company.getId() == id).findFirst());

        //when
        List<Employee> employees = companyService.getEmployeesByCompanyId(id);

        //then
        assertEquals(3, employees.get(0).getId());
        assertEquals(4, employees.get(1).getId());
        assertEquals(5, employees.get(2).getId());
    }
    @Test
    void should_return_company_id_2when_get_company_given_page_2_and_page_size_2() {
        //given
        int page = 2;
        int pageSize = 2;
        //when
        companyService.findAll(page, pageSize);

        //then
        Mockito.verify(mockedCompanyRepository).findAll(PageRequest.of(page, pageSize));
    }
    @Test
    void should_return_insert_company_when_insert_company_given_a_new_company() {
        //given

        List<Employee> forthEmployees = new ArrayList<>();
        forthEmployees.add(new Employee(9, "SCUT1", 20, "male", 6000));
        forthEmployees.add(new Employee(10, "SCUT2", 19, "female", 7000));
        Company newCompany = new Company(null, "SCUT", 2,  forthEmployees);
        when(mockedCompanyRepository.save(newCompany)).thenReturn(newCompany);

        //when
        Company returnCompany = companyService.save(newCompany);

        //then
        assertEquals(newCompany.getCompanyName(), returnCompany.getCompanyName());
    }

    @Test
    void should_return_updated_company_when_update_company_give_company_id_and_target_company() {
        //given
        int id = 1;
        Company targetCompany;
        targetCompany=new Company(1, "baiduu", 3, null);
        when(mockedCompanyRepository.findById(id)).thenReturn(generateCompanies().stream().filter(employee -> employee.getId() == id).findFirst());
        when(mockedCompanyRepository.save(targetCompany)).thenReturn(targetCompany);

        //when
        Company updatedCompany = companyService.updateEmployee(id, targetCompany);

        //then
        assertEquals(targetCompany.getId(), updatedCompany.getId());
        assertEquals(targetCompany.getCompanyName(), targetCompany.getCompanyName());
    }
    @Test
    void should_return_boolean_when_delete_company_given_id() {
        //given
        int id = 3;
        when(mockedCompanyRepository.findById(id)).thenReturn((new ArrayList<Company>()).stream().filter(employee -> employee.getId() == id).findFirst());

        //when
        boolean isDelete = companyService.deleteById(id);

        //then
        assertTrue(isDelete);
        Mockito.verify(mockedCompanyRepository).deleteById(id);

    }
}
