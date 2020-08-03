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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void should_get_employee_when_hit_get_employee_by_id_endpoint_given_id() throws Exception {
        //given
        Company company = new Company(null, "alibaba", 200, Collections.emptyList());
        company = companyRepository.save(company);
        Employee firstEmployee = new Employee(null, "ali1", 18, "male", 2000, company.getId());
        Employee secondEmployee = new Employee(null, "ali2", 20, "female", 2000, company.getId());
        employeeRepository.save(firstEmployee);
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


    @Test
    void should_get_the_third_and_forth_employees_when_hit_get_employee_specified_page_and_pageSize_endpoint_given_page_2_and_pageSize_2() throws Exception {
        //given
        Company company = new Company(null, "alibaba", 200, Collections.emptyList());
        company = companyRepository.save(company);
        Employee firstEmployee = new Employee(null, "ali1", 18, "male", 2000, company.getId());
        Employee secondEmployee = new Employee(null, "ali2", 20, "female", 2000, company.getId());
        Employee thirdEmployee = new Employee(null, "ali3", 18, "male", 2000, company.getId());
        Employee forthEmployee = new Employee(null, "ali4", 20, "female", 2000, company.getId());
        Employee fifthEmployee = new Employee(null, "ali5", 18, "male", 2000, company.getId());
        Employee sixthEmployee = new Employee(null, "ali6", 20, "female", 2000, company.getId());
        employeeRepository.save(firstEmployee);
        employeeRepository.save(secondEmployee);
        thirdEmployee = employeeRepository.save(thirdEmployee);
        forthEmployee = employeeRepository.save(forthEmployee);
        employeeRepository.save(fifthEmployee);
        employeeRepository.save(sixthEmployee);
        int page = 2;
        int pageSize = 2;

        //when

        String url = String.format("/employees?page=%s&pageSize=%s", page, pageSize);
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(thirdEmployee.getId()))
                .andExpect(jsonPath("$.content[0].name").value(thirdEmployee.getName()))
                .andExpect(jsonPath("$.content[0].age").value(thirdEmployee.getAge()))
                .andExpect(jsonPath("$.content[0].salary").value(thirdEmployee.getSalary()))
                .andExpect(jsonPath("$.content[0].gender").value(thirdEmployee.getGender()))
                .andExpect(jsonPath("$.content[0].companyId").value(thirdEmployee.getCompanyId()))
                .andExpect(jsonPath("$.content[1].id").value(forthEmployee.getId()))
                .andExpect(jsonPath("$.content[1].name").value(forthEmployee.getName()))
                .andExpect(jsonPath("$.content[1].age").value(forthEmployee.getAge()))
                .andExpect(jsonPath("$.content[1].salary").value(forthEmployee.getSalary()))
                .andExpect(jsonPath("$.content[1].gender").value(forthEmployee.getGender()))
                .andExpect(jsonPath("$.content[1].companyId").value(forthEmployee.getCompanyId()));

        //then
    }

    @Test
    void should_get_employees_when_hit_get_employee_by_gender_given_gender_male() throws Exception {
        //given
        Company company = new Company(null, "alibaba", 200, Collections.emptyList());
        company = companyRepository.save(company);
        Employee firstEmployee = new Employee(null, "ali1", 18, "male", 2000, company.getId());
        Employee secondEmployee = new Employee(null, "ali2", 20, "female", 2000, company.getId());
        Employee thirdEmployee = new Employee(null, "ali3", 18, "male", 2000, company.getId());
        Employee forthEmployee = new Employee(null, "ali4", 20, "female", 2000, company.getId());
        Employee fifthEmployee = new Employee(null, "ali5", 18, "female", 2000, company.getId());
        Employee sixthEmployee = new Employee(null, "ali6", 20, "female", 2000, company.getId());
        firstEmployee = employeeRepository.save(firstEmployee);
        employeeRepository.save(secondEmployee);
        thirdEmployee = employeeRepository.save(thirdEmployee);
        employeeRepository.save(forthEmployee);
        employeeRepository.save(fifthEmployee);
        employeeRepository.save(sixthEmployee);
        String gender = "male";

        //when

        String url = String.format("/employees?gender=%s", gender);
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(firstEmployee.getId()))
                .andExpect(jsonPath("$[0].name").value(firstEmployee.getName()))
                .andExpect(jsonPath("$[0].age").value(firstEmployee.getAge()))
                .andExpect(jsonPath("$[0].salary").value(firstEmployee.getSalary()))
                .andExpect(jsonPath("$[0].gender").value(firstEmployee.getGender()))
                .andExpect(jsonPath("$[0].companyId").value(firstEmployee.getCompanyId()))
                .andExpect(jsonPath("$[1].id").value(thirdEmployee.getId()))
                .andExpect(jsonPath("$[1].name").value(thirdEmployee.getName()))
                .andExpect(jsonPath("$[1].age").value(thirdEmployee.getAge()))
                .andExpect(jsonPath("$[1].salary").value(thirdEmployee.getSalary()))
                .andExpect(jsonPath("$[1].gender").value(thirdEmployee.getGender()))
                .andExpect(jsonPath("$[1].companyId").value(thirdEmployee.getCompanyId()));

        //then
    }

    @Test
    void should_add_employee_when_hit_post_employees_endpoints_given_employee_info() throws Exception {
        //given
        Company company = new Company(null, "alibaba", 200, Collections.emptyList());
        company = companyRepository.save(company);
        String employeeInfo = "{\n" +
                "    \"name\":\"Xiaoming\",\n" +
                "    \"age\":20,\n" +
                "    \"gender\":\"male\",\n" +
                "    \"salary\":10000,\n" +
                "    \"companyId\":" + company.getId() + "\n" +
                "}";
        //when
        mockMvc.perform(post(("/employees")).contentType(MediaType.APPLICATION_JSON).content(employeeInfo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Xiaoming"))
                .andExpect(jsonPath("$.age").value(20))
                .andExpect(jsonPath("$.salary").value(10000))
                .andExpect(jsonPath("$.gender").value("male"))
                .andExpect(jsonPath("$.companyId").value(company.getId()));


        //then
    }

    @Test
    void should_get_updated_employee_when_hit_put_employee_endpoint_given_id_and_employee_info() throws Exception {
        //given
        Company company = new Company(null, "alibaba", 200, Collections.emptyList());
        company = companyRepository.save(company);
        Employee employee = new Employee(null, "XiaoMing", 19, "female", 1000, company.getId());
        employee = employeeRepository.save(employee);
        String employeeInfo = "{\n" +
                "    \"id\":" + employee.getId() + ",\n" +
                "    \"name\":\"Xiaoming\",\n" +
                "    \"age\":20,\n" +
                "    \"gender\":\"male\",\n" +
                "    \"salary\":10000,\n" +
                "    \"companyId\":" + company.getId() + "\n" +
                "}";
        //when
        mockMvc.perform(patch(("/employees/" + employee.getId())).contentType(MediaType.APPLICATION_JSON).content(employeeInfo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Xiaoming"))
                .andExpect(jsonPath("$.age").value(20))
                .andExpect(jsonPath("$.salary").value(10000))
                .andExpect(jsonPath("$.gender").value("male"))
                .andExpect(jsonPath("$.companyId").value(company.getId()));


        //then
    }

    @Test
    void should_return_status_accepted_when_delete_a_employee_given_id() throws Exception {
        //given
        Company company = new Company(null, "alibaba", 200, Collections.emptyList());
        company = companyRepository.save(company);
        Employee employee = new Employee(null, "XiaoMing", 19, "female", 1000, company.getId());
        employee = employeeRepository.save(employee);
        //when
        mockMvc.perform(delete(("/employees/" + employee.getId())))
                .andExpect(status().isOk());

        //then
    }

    @Test
    void should_return_status_not_found_and_message_no_such_id_employee_when_when_hit_get_employee_by_id_endpoint_given_not_exist_id() throws Exception {
        //given
        int id = 100;

        //when
        mockMvc.perform(get("/employees/" + 100))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No such id employee"));

        //then
    }

    @Test
    void should_return_status_forbidden_and_message_id_is_not_corresponding_when_hit_put_employee_endpoint_given_employee_0_and_illegal_id_4() throws Exception {
        //given
        Company company = new Company(null, "alibaba", 200, Collections.emptyList());
        company = companyRepository.save(company);
        Employee employee = new Employee(null, "XiaoMing", 19, "female", 1000, company.getId());
        employee = employeeRepository.save(employee);
        String employeeInfo = "{\n" +
                "    \"id\":" + employee.getId() + ",\n" +
                "    \"name\":\"Xiaoming\",\n" +
                "    \"age\":20,\n" +
                "    \"gender\":\"male\",\n" +
                "    \"salary\":10000,\n" +
                "    \"companyId\":" + company.getId() + "\n" +
                "}";
        //when
        mockMvc.perform(patch(("/employees/" + employee.getId() + 1)).contentType(MediaType.APPLICATION_JSON).content(employeeInfo))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").value("id is not corresponding"));

        //then
    }

    @Test
    void should_return_status_not_found_and_message_no_such_id_employee_when_hit_put_employee_endpoint_given_not_exist_id() throws Exception {
        //given
        Company company = new Company(null, "alibaba", 200, Collections.emptyList());
        company = companyRepository.save(company);
        Employee employee = new Employee(null, "XiaoMing", 19, "female", 1000, company.getId());
        employee = employeeRepository.save(employee);
        String employeeInfo = "{\n" +
                "    \"id\":" + employee.getId() + 1 + ",\n" +
                "    \"name\":\"Xiaoming\",\n" +
                "    \"age\":20,\n" +
                "    \"gender\":\"male\",\n" +
                "    \"salary\":10000,\n" +
                "    \"companyId\":" + company.getId() + "\n" +
                "}";
        //when
        mockMvc.perform(patch(("/employees/" + employee.getId() + 1)).contentType(MediaType.APPLICATION_JSON).content(employeeInfo))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No such id employee"));

        //then
    }

    @Test
    void should_return_status_not_found_and_message_no_such_id_employee_when_delete_employee_given_not_exist_id() throws Exception {
        //given
        Company company = new Company(null, "alibaba", 200, Collections.emptyList());
        company = companyRepository.save(company);
        Employee employee = new Employee(null, "XiaoMing", 19, "female", 1000, company.getId());
        employee = employeeRepository.save(employee);
        //when
        mockMvc.perform(delete(("/employees/" + employee.getId())))
                .andExpect(status().isOk());

        //then
    }
}
