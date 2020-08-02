package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.exception.IllegalOperationException;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
import com.thoughtworks.springbootemployee.mapper.EmployeeMapper;
import com.thoughtworks.springbootemployee.mapper.request.EmployeeRequest;
import com.thoughtworks.springbootemployee.mapper.response.EmployeeResponse;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private final EmployeeService employeeService;
    @Autowired
    private final EmployeeMapper employeeMapper;

    public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse addEmployee(@RequestBody EmployeeRequest employeeRequest) {
        Employee employee = employeeMapper.EmployeeRequestToEmployee(employeeRequest);
        return employeeService.save(employee);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse getEmployeeById(@PathVariable int id) throws NoSuchDataException {
        return employeeService.findById(id);
    }

    @GetMapping(params = {"page", "pageSize"})
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeResponse> getEmployeesPagination(int page, int pageSize) {
        return employeeService.findAll(page, pageSize);
    }

    @GetMapping(params = {"gender"})
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeResponse> getEmployeesByGender(String gender) {
        return employeeService.findAllByGender(gender);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse updateEmployee(@PathVariable int id, @RequestBody EmployeeRequest employeeRequest) throws NoSuchDataException, IllegalOperationException {
        Employee employee = employeeMapper.EmployeeRequestToEmployee(employeeRequest);
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteEmployee(@PathVariable int id) throws NoSuchDataException {
        employeeService.deleteById(id);
    }
}
