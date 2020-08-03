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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

public class CompanyServiceTest {
    private CompanyRepository mockedCompanyRepository;
    private CompanyService companyService;
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private EmployeeMapper employeeMapper;


    @BeforeEach
    void init() {
        this.companyMapper = new CompanyMapper();
        this.employeeMapper = new EmployeeMapper();
        mockedCompanyRepository = Mockito.mock(CompanyRepository.class);
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        companyService = new CompanyService(mockedCompanyRepository, companyMapper, employeeMapper, employeeRepository);
    }


    @Test
    void should_return_3_companies_when_get_all_companies_given_no_parameter() {
        //given
        List<Company> companies = new ArrayList<>();
        List<Employee> firstEmployees = new ArrayList<>();
        firstEmployees.add(new Employee(0, "alibaba1", 20, "male", 6000));
        companies.add(new Company(0, "alibaba", firstEmployees));

        List<Employee> secondEmployees = new ArrayList<>();
        secondEmployees.add(new Employee(4, "baidu1", 20, "male", 6000));
        companies.add(new Company(1, "baidu", secondEmployees));

        List<Employee> thirdEmployees = new ArrayList<>();
        thirdEmployees.add(new Employee(6, "tencent1", 20, "male", 6000));
        companies.add(new Company(2, "tencent", thirdEmployees));
        when(mockedCompanyRepository.findAll()).thenReturn(companies);

        //when
        List<CompanyResponse> foundCompanies = companyService.findAll();

        //then
        assertEquals(3, foundCompanies.size());
    }

    @Test
    void should_return_id_1_company_when_get_company_by_id_given_1() throws NoSuchDataException {
        //given
        List<Employee> secondEmployees = new ArrayList<>();
        secondEmployees.add(new Employee(3, "baidu1", 20, "male", 6000));
        Company mockedCompany = new Company(1, "baidu", secondEmployees);
        int id = 1;
        given(mockedCompanyRepository.findById(id)).willReturn(java.util.Optional.of(mockedCompany));

        //when
        CompanyResponse companyResponse = companyService.findById(id);

        //then
        assertEquals("baidu", companyResponse.getCompanyName());
        assertEquals(1, companyResponse.getId());

    }

    @Test
    void should_return_baidu_employees_when_get_employee_by_id_given_1() {
        //given

        List<Employee> secondEmployees = new ArrayList<>();
        secondEmployees.add(new Employee(3, "baidu1", 20, "male", 6000));
        secondEmployees.add(new Employee(4, "baidu2", 19, "female", 7000));
        secondEmployees.add(new Employee(5, "baidu3", 19, "male", 8000));
        Company mockedCompany = new Company(1, "baidu", secondEmployees);
        int id = 1;
        when(mockedCompanyRepository.findById(id)).thenReturn(java.util.Optional.of(mockedCompany));

        //when
        List<EmployeeResponse> employeeResponses = companyService.getEmployeesByCompanyId(id);

        //then
        assertEquals(3, employeeResponses.get(0).getId());
        assertEquals(4, employeeResponses.get(1).getId());
        assertEquals(5, employeeResponses.get(2).getId());
    }

    @Test
    void should_return_company_id_2_and_3_when_get_company_given_page_2_and_page_size_2() {
        //given
        int page = 2;
        int pageSize = 2;
        List<Company> mockedCompanies = new ArrayList<>();
        List<Employee> firstEmployees = new ArrayList<>();
        List<Employee> thirdEmployees = new ArrayList<>();
        thirdEmployees.add(new Employee(6, "tencent1", 20, "male", 6000));
        mockedCompanies.add(new Company(2, "tencent", thirdEmployees));

        List<Employee> forthEmployees = new ArrayList<>();
        forthEmployees.add(new Employee(9, "oocl1", 20, "male", 6000));
        mockedCompanies.add(new Company(3, "oocl", forthEmployees));

        given(mockedCompanyRepository.findAll(PageRequest.of(page - 1, pageSize)))
                .willReturn(new PageImpl<>(mockedCompanies));

        //when
        List<CompanyResponse> companyResponsess = companyService.findAll(page, pageSize).getContent();

        //then
        assertEquals(2, companyResponsess.get(0).getId());
        assertEquals(3, companyResponsess.get(1).getId());
    }
    @Test
    void should_return_insert_company_when_insert_company_given_a_new_company() {
        //given

        List<Employee> newEmployees = new ArrayList<>();
        newEmployees.add(new Employee(12, "SCUT1", 20, "male", 6000));
        newEmployees.add(new Employee(13, "SCUT2", 19, "female", 7000));
        Company newCompany = new Company(null, "SCUT", newEmployees);
        Company mockedCompany = new Company(4, "SCUT", newEmployees);
        when(mockedCompanyRepository.save(newCompany)).thenReturn(mockedCompany);

        //when
        CompanyResponse companyResponse = companyService.save(newCompany);

        //then
        assertEquals(mockedCompany.getCompanyName(), companyResponse.getCompanyName());
        assertEquals(4, companyResponse.getId());
    }

    @Test
    void should_return_updated_company_when_update_company_give_company_id_and_target_company() throws IllegalOperationException, NoSuchDataException {
        //given
        int id = 1;
        Company mockedCompany = new Company(1, "TW", null);
        when(mockedCompanyRepository.findById(id)).thenReturn(java.util.Optional.of(mockedCompany));
        when(mockedCompanyRepository.save(mockedCompany)).thenReturn(mockedCompany);

        //when
        CompanyResponse companyResponse = companyService.updateEmployee(id, mockedCompany);

        //then
        assertEquals(mockedCompany.getId(), companyResponse.getId());
        assertEquals(mockedCompany.getCompanyName(), companyResponse.getCompanyName());
    }

    @Test
    void should_deleteById_run_a_time__when_delete_company_given_id() throws NoSuchDataException {
        //given
        List<Employee> forthEmployees = new ArrayList<>();
        forthEmployees.add(new Employee(9, "oocl1", 20, "male", 6000));
        Company mockedCompany = new Company(3, "oocl", forthEmployees);
        int id = 3;
        when(mockedCompanyRepository.findById(id)).thenReturn(java.util.Optional.of(mockedCompany));

        //when
        companyService.deleteById(id);

        //then
        Mockito.verify(mockedCompanyRepository).deleteById(id);

    }

    @Test
    void should_throw_no_such_data_exception_when_find_by_id_given_not_exist_id() {
        //given
        int notExistId = 3;
        given(mockedCompanyRepository.findById(notExistId)).willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(NoSuchDataException.class, () -> companyService.findById(notExistId));

        //then
        assertEquals(NoSuchDataException.class, exception.getClass());
    }

    @Test
    void should_throw_illegal_operation_exception_when_update_companies_given_illegal_id_4_and_employee_id_0() {
        //given
        int illegalId = 4;
        Company newCompany = new Company(0, "TW", null);
        when(mockedCompanyRepository.findById(illegalId)).thenReturn(Optional.empty());
        //when
        Exception exception = assertThrows(IllegalOperationException.class, () -> companyService.updateEmployee(illegalId, newCompany));

        //then
        assertEquals(IllegalOperationException.class, exception.getClass());
    }

    @Test
    void should_throw_no_such_data_exception_when_update_company_given_not_exist_id() {
        //given
        int id = 0;
        Company newCompany = new Company(0, "TW", null);
        given(mockedCompanyRepository.findById(id)).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(NoSuchDataException.class, () -> companyService.updateEmployee(id, newCompany));

        //then
        assertEquals(NoSuchDataException.class, exception.getClass());
    }

    @Test
    void should_throw_no_such_data_exception_when_delete_company_given_not_exist_id() {
        //given
        int id = 5;
        Employee newEmployee = new Employee(5, "Gavin", 20, "male", 9000);
        given(mockedCompanyRepository.findById(id)).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(NoSuchDataException.class, () -> companyService.deleteById(id));

        //then
        assertEquals(NoSuchDataException.class, exception.getClass());

    }
}