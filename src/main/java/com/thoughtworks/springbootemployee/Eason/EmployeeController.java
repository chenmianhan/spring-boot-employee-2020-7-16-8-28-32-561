package com.thoughtworks.springbootemployee.Eason;

import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

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
    public Employee getEmployeeById(@PathVariable int id) {
        return employeeService.findById(id);
    }

    @GetMapping(params = {"page", "pageSize"})

    public List<Employee> getEmployeesPagination(int page, int pageSize) {
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            employees.add(new Employee(i, "alibaba3", 19, "male", 8000));
        }
        return employees;
    }

    @GetMapping(params = {"gender"})
    public List<Employee> getEmployeesByGender(String gender) {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(4, "alibaba1", 20, "male", 6000));
        employees.add(new Employee(11, "tengxun2", 19, "female", 7000));
        employees.add(new Employee(6, "alibaba3", 19, "male", 8000));
        return employees.stream().filter(employee -> employee.getGender().equals(gender)).collect(Collectors.toList());
    }



    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
        Employee newEmployee = new Employee();
        newEmployee.setId(employee.getId());
        newEmployee.setName(employee.getName());
        newEmployee.setGender(employee.getGender());
        newEmployee.setAge(employee.getAge());
        newEmployee.setSalary(employee.getSalary());
        return newEmployee;
    }

    @DeleteMapping("/{id}")
    public Employee deleteEmployee(@PathVariable int id) {
        return new Employee(id, "alibaba3", 19, "male", 8000);
    }
}
