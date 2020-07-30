package com.thoughtworks.springbootemployee.integrationTest;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeIntegrationTest {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Test
    void should_get_employee_when_hit_get_employees_endpoint_given_nothing() throws Exception {
        //given
        Company company = new Company(null, "alibaba", 200, Collections.emptyList());
        company = companyRepository.save(company);
        Employee employee = new Employee(null, "alibaba1", 18, "male", 1000, company.getId());
        employee = employeeRepository.save(employee);

        //when
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").value(employee.getName()))
                .andExpect(jsonPath("$[0].age").value(employee.getAge()))
                .andExpect(jsonPath("$[0].salary").value(employee.getSalary()))
                .andExpect(jsonPath("$[0].gender").value(employee.getGender()))
                .andExpect(jsonPath("$[0].companyId").value(employee.getCompanyId()));


        //then
    }

    @Test
    void should_get_employee_when_hit_get_employee_by_id_given_id() throws Exception {
        //given
        Company company = new Company(null, "alibaba", 200, Collections.emptyList());
        company = companyRepository.save(company);
        Employee firstEmployee = new Employee(null, "ali1", 18, "male", 2000, company.getId());
        Employee secondEmployee = new Employee(null, "ali2", 20, "female", 2000, company.getId());
        firstEmployee = employeeRepository.save(firstEmployee);
        secondEmployee = employeeRepository.save(secondEmployee);

        //when
        mockMvc.perform(get("/employees/" + secondEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(secondEmployee.getName()))
                .andExpect(jsonPath("$.age").value(secondEmployee.getAge()))
                .andExpect(jsonPath("$.salary").value(secondEmployee.getSalary()))
                .andExpect(jsonPath("$.gender").value(secondEmployee.getGender()))
                .andExpect(jsonPath("$.companyId").value(secondEmployee.getCompanyId()));

        //then
    }


}
