package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.exception.IllegalOperationException;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
import com.thoughtworks.springbootemployee.mapper.CompanyMapper;
import com.thoughtworks.springbootemployee.mapper.request.CompanyRequest;
import com.thoughtworks.springbootemployee.mapper.response.CompanyResponse;
import com.thoughtworks.springbootemployee.mapper.response.EmployeeResponse;
import com.thoughtworks.springbootemployee.model.Company;
import com.thoughtworks.springbootemployee.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    @Autowired
    private final CompanyService companyService;

    @Autowired
    private final CompanyMapper companyMapper;

    public CompanyController(CompanyService companyService, CompanyMapper companyMapper) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse addCompany(@RequestBody CompanyRequest companyRequest) {
        return companyService.save(companyMapper.CompanyRequestToCompany(companyRequest));
    }

    @GetMapping(params = {"page", "pageSize"})
    @ResponseStatus(HttpStatus.OK)
    public Page<CompanyResponse> getCompaniesByPageAndPageSize(int page, int pageSize) {

        return companyService.findAll(page, pageSize);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompanyResponse> getAllCompanies() {
        return companyService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompanyResponse getCompanyById(@PathVariable int id) throws NoSuchDataException {
        return companyService.findById(id);
    }

    @GetMapping("/{id}/employees")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> getEmployeesByCompanyId(@PathVariable int id) {
        return companyService.getEmployeesByCompanyId(id);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse updateCompany(@PathVariable int id, @RequestBody CompanyRequest newCompanyRequest) throws IllegalOperationException, NoSuchDataException {

        Company newCompany = companyMapper.CompanyRequestToCompany(newCompanyRequest);
        return companyService.updateEmployee(id, newCompany);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAllEmployeesByCompanyId(@PathVariable int id) throws NoSuchDataException {

        companyService.deleteById(id);
    }
}
