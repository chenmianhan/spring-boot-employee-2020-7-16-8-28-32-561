package com.thoughtworks.springbootemployee.mapper;

import com.thoughtworks.springbootemployee.mapper.request.CompanyRequest;
import com.thoughtworks.springbootemployee.mapper.response.CompanyResponse;
import com.thoughtworks.springbootemployee.model.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    public Company CompanyRequestToCompany(CompanyRequest companyRequest) {
        Company company = new Company();
        company.setId(companyRequest.getId());
        company.setCompanyName(companyRequest.getCompanyName());
        company.setEmployees(companyRequest.getEmployees());
        return company;
    }

    public CompanyResponse CompanyToCompanyResponse(Company company) {
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setId(company.getId());
        companyResponse.setCompanyName(company.getCompanyName());
        if (company.getEmployees() == null)
            companyResponse.setEmployeeNumber(0);
        else companyResponse.setEmployeeNumber(company.getEmployees().size());
        companyResponse.setEmployees(company.getEmployees());
        return companyResponse;
    }
}
