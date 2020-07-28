package com.thoughtworks.springbootemployee.Eason;

import com.thoughtworks.springbootemployee.model.Employee;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(params = {"gender"})
    public List<Employee> getEmployeesByGender(String gender) {
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            employees.add(new Employee(i, "male"));
        }
        return employees;
    }

    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        return new Employee(employee.getId(), employee.getGender());
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
        Employee newEmployee = new Employee(id);
        newEmployee.setId(employee.getId());
        newEmployee.setGender(employee.getGender());
        return newEmployee;
    }

    @DeleteMapping("/{id}")
    public Employee deleteEmployee(@PathVariable int id) {
        return new Employee(id);
    }
}
