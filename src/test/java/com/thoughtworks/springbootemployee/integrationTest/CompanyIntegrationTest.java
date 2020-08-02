package com.thoughtworks.springbootemployee.integrationTest;

import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyIntegrationTest {
    @Autowired
    CompanyRepository companyRepository;
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
        companies.add(new Company(null, "huawei", 10, null));
        companies.add(new Company(null, "alibaba", 10, null));
        companyRepository.saveAll(companies);

        //when
        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].companyName").value("huawei"))
                .andExpect(jsonPath("$[0].employeeNumber").value(10))
                .andExpect(jsonPath("$[0].employees").value(empty()))
                .andExpect(jsonPath("$[1].id").isNumber())
                .andExpect(jsonPath("$[1].companyName").value("alibaba"))
                .andExpect(jsonPath("$[1].employeeNumber").value(10))
                .andExpect(jsonPath("$[1].employees").value(empty()));
        //then
    }
}
