package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.IllegalOperationException;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
import com.thoughtworks.springbootemployee.mapper.EmployeeMapper;
import com.thoughtworks.springbootemployee.mapper.response.EmployeeResponse;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public List<EmployeeResponse> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResponse> employeeResponses = new LinkedList<>();
        for (Employee employee : employees) {
            employeeResponses.add(employeeMapper.EmployeeToEmployeeResponse(employee));
        }
        return employeeResponses;
    }

    public Page<EmployeeResponse> findAll(int page, int pageSize) {
        Page<Employee> employeePage = employeeRepository.findAll(PageRequest.of(page - 1, pageSize));
        List<Employee> employees = employeePage.getContent();
        List<EmployeeResponse> employeeResponses = new LinkedList<>();
        for (Employee employee : employees) {
            employeeResponses.add(employeeMapper.EmployeeToEmployeeResponse(employee));
        }
        return new PageImpl<>(employeeResponses);
    }

    public EmployeeResponse findById(int id) throws NoSuchDataException {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (!employeeOptional.isPresent())
            throw new NoSuchDataException("No such id employee");
        else
            return employeeMapper.EmployeeToEmployeeResponse(employeeOptional.get());
    }

    public List<EmployeeResponse> findAllByGender(String gender) {
        List<Employee> employees = employeeRepository.findAllByGender(gender);
        List<EmployeeResponse> employeeResponses = new LinkedList<>();
        for (Employee employee : employees) {
            employeeResponses.add(employeeMapper.EmployeeToEmployeeResponse(employee));
        }
        return employeeResponses;
    }

    public EmployeeResponse save(Employee newEmployee) {
        return employeeMapper.EmployeeToEmployeeResponse(employeeRepository.save(newEmployee));
    }

    public EmployeeResponse updateEmployee(int id, Employee newEmployee) throws NoSuchDataException, IllegalOperationException {
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
        ///TODO 不建议直接赋值
        oldEmployee = employeeRepository.save(oldEmployee);
        return employeeMapper.EmployeeToEmployeeResponse(oldEmployee);
    }

    public void deleteById(int id) throws NoSuchDataException {
        if (!employeeRepository.findById(id).isPresent())
            throw new NoSuchDataException("No such id employee");
        employeeRepository.deleteById(id);
    }
}
