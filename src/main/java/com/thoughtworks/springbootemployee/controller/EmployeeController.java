package com.thoughtworks.springbootemployee.controller;

import com.thoughtworks.springbootemployee.exception.IllegalOperationException;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
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

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Employee getEmployeeById(@PathVariable int id) throws NoSuchDataException {
        return employeeService.findById(id);
    }

    @GetMapping(params = {"page", "pageSize"})
    @ResponseStatus(HttpStatus.OK)
    public Page<Employee> getEmployeesPagination(int page, int pageSize) {
        return employeeService.findAll(page,pageSize);
    }

    @GetMapping(params = {"gender"})
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getEmployeesByGender(String gender) {
        return employeeService.findAllByGender(gender);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee updateEmployee(@PathVariable int id, @RequestBody Employee employee) throws NoSuchDataException, IllegalOperationException {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteEmployee(@PathVariable int id) {
        employeeService.deleteById(id);
    }
}
