package com.thoughtworks.springbootemployee.Eason;

import com.thoughtworks.springbootemployee.model.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @GetMapping
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1));
        employees.add(new Employee(2));
        return employees;
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        return new Employee(id);
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<Employee> getEmployeesPagination(int page, int pageSize) {
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            employees.add(new Employee(i));
        }
        return employees;
    }
}
