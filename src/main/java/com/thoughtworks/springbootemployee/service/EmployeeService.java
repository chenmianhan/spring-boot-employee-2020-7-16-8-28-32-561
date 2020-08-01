package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.IllegalOperationException;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Page<Employee> findAll(int page, int pageSize) {
        return employeeRepository.findAll(PageRequest.of(page - 1, pageSize));
    }

    public Employee findById(int id) throws NoSuchDataException {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (!employeeOptional.isPresent())
            throw new NoSuchDataException("No such id employee");
        else
            return employeeOptional.get();
    }

    public List<Employee> findAllByGender(String gender) {
        return employeeRepository.findAllByGender(gender);
    }

    public Employee save(Employee newEmployee) {
        return employeeRepository.save(newEmployee);
    }

    public Employee updateEmployee(int id, Employee newEmployee) throws NoSuchDataException, IllegalOperationException {
        if (id != newEmployee.getId()) {
            throw new IllegalOperationException("id is not corresponding");
        }
        Optional<Employee> oldEmployeeOptional = employeeRepository.findById(id);
        if (!oldEmployeeOptional.isPresent())
            throw new NoSuchDataException("No such id employee");
        Employee oldEmployee = oldEmployeeOptional.get();
        if (newEmployee.getName() != null)
            oldEmployee.setName(newEmployee.getName());
        if (newEmployee.getGender() != null)
            oldEmployee.setGender(newEmployee.getGender());
        if (newEmployee.getAge() != null)
            oldEmployee.setAge(newEmployee.getAge());
        if (newEmployee.getSalary() != null)
            oldEmployee.setSalary(newEmployee.getSalary());
        oldEmployee = save(oldEmployee);
        return oldEmployee;
    }

    public void deleteById(int id) throws NoSuchDataException {
        if (!employeeRepository.findById(id).isPresent())
            throw new NoSuchDataException("No such id employee");
        employeeRepository.deleteById(id);
    }
}
