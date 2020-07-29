package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Test
    void should_return_3_size_employee_list_when_find_by_gender_given_gender_is_male() {
        //given
        String gender = "Male";
        when(mockedEmployeeRepository.findAllByGender(gender)).thenReturn(generateEmployees().stream().filter(employee -> employee.getGender().equals(gender)).collect(Collectors.toList()));

        //when
        List<Employee> maleEmployees = employeeService.findAllByGender(gender);

        //then
        assertEquals(3, maleEmployees.size());
    }

    @Test
    void should_return_inserted_employee_when_insert_employee_given_a_new_employee() {
        //given
        Employee newEmployee = new Employee(null, "Xiaohei", 15, "Female", 8000);
        when(mockedEmployeeRepository.save(newEmployee)).thenReturn(new Employee(5, "Xiaohei", 15, "Female", 8000));


        //when
        Employee returnEmployee = employeeService.save(newEmployee);

        //then
        assertEquals(5, returnEmployee.getId());
        assertEquals(newEmployee.getAge(), returnEmployee.getAge());
        assertEquals(newEmployee.getGender(), returnEmployee.getGender());
        assertEquals(newEmployee.getName(), returnEmployee.getName());
        assertEquals(newEmployee.getSalary(), returnEmployee.getSalary());
    }
}
