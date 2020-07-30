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
        return employeeRepository.findAll(PageRequest.of(page, pageSize));
    }

    public Employee findById(int id) throws NoSuchDataException {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (!employeeOptional.isPresent())
            throw new NoSuchDataException();
        else
            return employeeOptional.get();
    }

    public List<Employee> findAllByGender(String gender) {
        return employeeRepository.findAllByGender(gender);
    }

    public Employee save(Employee newEmployee) {
        return employeeRepository.save(newEmployee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) throws NoSuchDataException, IllegalOperationException {
        if (id != updatedEmployee.getId()) {
            throw new IllegalOperationException();
        }
        //TODO
        Optional<Employee> targetEmployeeOptional = employeeRepository.findById(id);
        if (!targetEmployeeOptional.isPresent())
            throw new NoSuchDataException();
        Employee targetEmployee = targetEmployeeOptional.get();
        if (updatedEmployee.getName() != null)
            targetEmployee.setName(updatedEmployee.getName());
        if (updatedEmployee.getGender() != null)
            targetEmployee.setGender(updatedEmployee.getGender());
        if (updatedEmployee.getAge() != null)
            targetEmployee.setAge(updatedEmployee.getAge());
        if (updatedEmployee.getSalary() != null)
            targetEmployee.setSalary(updatedEmployee.getSalary());
        //TODO
        targetEmployee = save(targetEmployee);
        return targetEmployee;
    }

    public boolean deleteById(int id) {
        //TODO
        employeeRepository.deleteById(id);

        return !employeeRepository.findById(id).isPresent();
    }
}
