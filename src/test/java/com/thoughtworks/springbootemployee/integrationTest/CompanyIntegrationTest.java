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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyIntegrationTest {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        companyRepository.deleteAll();
    }

    @Test
    void should_get_companies_when_hit_get_companies_given_nothing() throws Exception {
        //given
        List<Company> companies = new ArrayList<>();
        companies.add(new Company(null, "huawei", null));
        companies.add(new Company(null, "alibaba", null));
        companyRepository.saveAll(companies);

        //when
        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].companyName").value("huawei"))
                .andExpect(jsonPath("$[0].employeeNumber").value(0))
                .andExpect(jsonPath("$[0].employees").value(empty()))
                .andExpect(jsonPath("$[1].id").isNumber())
                .andExpect(jsonPath("$[1].companyName").value("alibaba"))
                .andExpect(jsonPath("$[1].employeeNumber").value(0))
                .andExpect(jsonPath("$[1].employees").value(empty()));
        //then
    }

    @Test
    void should_get_company_when_hit_get_company_by_id_endpoint_given_id() throws Exception {
        //given
        Company mockedCompany = new Company(null, "baidu", null);
        mockedCompany = companyRepository.save(mockedCompany);

        //when
        mockMvc.perform(get("/companies/" + mockedCompany.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.companyName").value(mockedCompany.getCompanyName()))
                .andExpect(jsonPath("$.employeeNumber").value(0))
                .andExpect(jsonPath("$.employees").value(empty()));

        //then
    }

    @Test
    void should_get_company_employees_when_hit_get_employee_of_company_by_id_endpoint_given_id() throws Exception {
        //given
        Company company = new Company(null, "oocl", null);
        company = companyRepository.save(company);
        Employee employee = new Employee(null, "James", 18, "male", 10000, company.getId());
        employeeRepository.save(employee);

        //when
        mockMvc.perform(get(String.format("/companies/%s/employees", company.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").value("James"))
                .andExpect(jsonPath("$[0].age").value(18))
                .andExpect(jsonPath("$[0].gender").value("male"))
                .andExpect(jsonPath("$[0].companyId").value(company.getId()));
        //then
    }

    @Test
    void should_get_the_third_and_forth_company_when_hit_get_company_specified_page_and_pageSize_endpoint_given_page_2_and_pageSize_2() throws Exception {
        //given
        int page = 2;
        int pageSize = 2;
        Company firstCompany = new Company(null, "alibaba", null);
        companyRepository.save(firstCompany);
        Company secondCompany = new Company(null, "baidu", null);
        companyRepository.save(secondCompany);
        Company thirdCompany = new Company(null, "tencent", null);
        thirdCompany = companyRepository.save(thirdCompany);
        Company forthCompany = new Company(null, "oocl", null);
        forthCompany = companyRepository.save(forthCompany);

        //when
        mockMvc.perform(get(String.format("/companies?page=%s&&pageSize=%s", page, pageSize)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(thirdCompany.getId()))
                .andExpect(jsonPath("$.content[0].companyName").value("tencent"))
                .andExpect(jsonPath("$.content[1].id").value(forthCompany.getId()))
                .andExpect(jsonPath("$.content[1].companyName").value("oocl"));
    }

    @Test
    void should_add_company_when_hit_post_companies_endpoints_given_company_info() throws Exception {
        //given
        String companyInfo = "{\n" +
                "\"companyName\":\"scut\",\n" +
                "\"employees\":null\n" +
                "\n" +
                "}";
        //when
        mockMvc.perform(post(("/companies")).contentType(MediaType.APPLICATION_JSON).content(companyInfo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.companyName").value("scut"))
                .andExpect(jsonPath("$.employeeNumber").value(0));
        //then
    }

    @Test
    void should_get_updated_company_when_hit_put_employee_endpoint_given_id_and_employee_info() throws Exception {
        //given
        Company company = new Company(null, "SCUT", null);
        company = companyRepository.save(company);
        String companyInfo = "{\n" +
                "    \"id\":" + company.getId() + ",\n" +
                "\"companyName\":\"TW\",\n" +
                "\"employees\":null\n" +
                "\n" +
                "}";
        //when
        mockMvc.perform(patch(("/companies/" + company.getId())).contentType(MediaType.APPLICATION_JSON).content(companyInfo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.companyName").value("TW"))
                .andExpect(jsonPath("$.employeeNumber").value(0));
        //then
    }

    @Test
    void should_return_status_accepted_when_delete_a_company_given_id() throws Exception {
        //given
        Company company = new Company(null, "SCUT", null);
        company = companyRepository.save(company);
        //when
        mockMvc.perform(delete(("/companies/" + company.getId())))
                .andExpect(status().isOk());
        //then
    }

    @Test
    void should_return_status_not_found_and_message_no_such_id_company_when_when_hit_get_company_by_id_endpoint_given_not_exist_id() throws Exception {
        //given
        int id = 100;

        //when
        mockMvc.perform(get("/companies/" + 100))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No such id company"));

        //then
    }

    @Test
    void should_return_status_forbidden_and_message_id_is_not_corresponding_when_hit_put_company_endpoint_given_company_0_and_illegal_id_4() throws Exception {
        //given
        Company company = new Company(0, "alibaba", Collections.emptyList());
        String companyInfo = "{\n" +
                "    \"id\":" + company.getId() + ",\n" +
                "\"companyName\":\"alibaba\",\n" +
                "\"employees\":null\n" +
                "\n" +
                "}";
        //when
        mockMvc.perform(patch(("/companies/" + 4)).contentType(MediaType.APPLICATION_JSON).content(companyInfo))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").value("id is not corresponding"));

        //then
    }

    @Test
    void should_return_status_not_found_and_message_no_such_id_company_when_hit_put_company_endpoint_given_not_exist_id() throws Exception {
        //given
        Company company = new Company(0, "alibaba", Collections.emptyList());
        String companyInfo = "{\n" +
                "    \"id\":" + company.getId() + ",\n" +
                "\"companyName\":\"alibaba\",\n" +
                "\"employees\":null\n" +
                "\n" +
                "}";
        //when
        mockMvc.perform(patch(("/companies/" + 0)).contentType(MediaType.APPLICATION_JSON).content(companyInfo))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("No such id company"));

        //then
    }

    @Test
    void should_return_status_not_found_and_message_no_such_id_company_when_delete_company_given_not_exist_id() throws Exception {
        //given
        int id = 1;
        //when
        mockMvc.perform(delete(("/companies/" + 1)))
                .andExpect(status().isNotFound());
        //then
    }
}
