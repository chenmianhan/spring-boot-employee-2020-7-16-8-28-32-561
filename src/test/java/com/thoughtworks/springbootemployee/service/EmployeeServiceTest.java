package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class EmployeeServiceTest {
    private EmployeeRepository mockedEmployeeRepository;
    private EmployeeService employeeService;

    @BeforeEach
    void init() {
        mockedEmployeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeService(mockedEmployeeRepository);
    }

    private List<Employee> generateEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(0, "Xiaoming", 20, "Male", 10000));
        employees.add(new Employee(1, "Xiaohong", 19, "Female", 10000));
        employees.add(new Employee(2, "Xiaozhi", 15, "Male", 10000));
        employees.add(new Employee(3, "Xiaogang", 16, "Male", 10000));
        employees.add(new Employee(4, "Xiaoxia", 15, "Female", 10000));
        return employees;
    }

    @Test
    void should_return_all_employees_when_get_all_employee_given_no_parameter() {
        //given
        when(mockedEmployeeRepository.findAll()).thenReturn(generateEmployees());

        //when
        List<Employee> employees = employeeService.findAll();

        //then
        assertEquals(5, employees.size());
    }

    @Test
    void should_return_Xiaoming_when_get_employee_by_id_given_0() {
        //given
        int id = 0;
        when(mockedEmployeeRepository.findById(id)).thenReturn(generateEmployees().stream().filter(employee -> employee.getId() == id).findFirst());

        //when
        Employee employee = employeeService.findById(id);

        //then
        assertEquals("Xiaoming", employee.getName());
    }

    @Test
    void should_return_employees_in_the_specified_range_when_get_employees_given_page_2_and_page_size_2() {
        //given
        int page = 2;
        int pageSize = 2;
        //when
        employeeService.findAll(page, pageSize);

        //then
        Mockito.verify(mockedEmployeeRepository).findAll(PageRequest.of(page, pageSize));
    }
}
