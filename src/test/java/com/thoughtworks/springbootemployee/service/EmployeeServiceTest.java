package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.exception.IllegalOperationException;
import com.thoughtworks.springbootemployee.exception.NoSuchDataException;
import com.thoughtworks.springbootemployee.model.Employee;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
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
    void should_return_6_employees_when_get_all_employee_given_no_parameter() {
        //given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(0, "James", 20, "male", 10000));
        employees.add(new Employee(1, "Penny", 19, "female", 10000));
        employees.add(new Employee(2, "Spike", 15, "male", 10000));
        employees.add(new Employee(3, "Albert", 20, "male", 10000));
        employees.add(new Employee(4, "Quentin", 19, "male", 10000));
        employees.add(new Employee(5, "Gavin", 15, "male", 10000));
        when(mockedEmployeeRepository.findAll()).thenReturn(employees);

        //when
        List<Employee> foundEmployees = employeeService.findAll();

        //then
        assertEquals(6, foundEmployees.size());
    }

    @Test
    void should_return_James_when_get_employee_by_id_given_0should_return_James_when_get_employee_by_id_given_0() throws NoSuchDataException {
        //given
        int id = 0;
        when(mockedEmployeeRepository.findById(id)).thenReturn(Optional.of(new Employee(0, "James", 20, "Male", 10000)));

        //when
        Employee employee = employeeService.findById(id);

        //then
        assertEquals("James", employee.getName());
    }

    @Test
    void should_return_employees_id_2_and_3_when_get_employees_given_page_2_and_page_size_2() {
        //given
        int page = 2;
        int pageSize = 2;
        List<Employee> employees = new LinkedList<>();
        employees.add(new Employee(2, "Spike", 15, "male", 10000));
        employees.add(new Employee(3, "Albert", 15, "male", 10000));
        given(mockedEmployeeRepository.findAll(PageRequest.of(page - 1, pageSize)))
                .willReturn(new PageImpl<>(employees));

        //when
        List<Employee> resultEmployees = employeeService.findAll(page, pageSize).getContent();

        //then
        assertEquals(2, resultEmployees.get(0).getId());
        assertEquals(3, resultEmployees.get(1).getId());

    }

    @Test
    void should_return_1_employee_list_when_find_by_gender_given_gender_is_male() {
        //given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "Penny", 19, "female", 10000));
        String gender = "female";
        given(mockedEmployeeRepository.findAllByGender(gender)).willReturn(employees);

        //when
        List<Employee> femaleEmployees = employeeService.findAllByGender(gender);

        //then
        assertEquals(1, femaleEmployees.size());
    }

    @Test
    void should_return_inserted_employee_when_insert_employee_given_a_new_employee() {
        //given
        Employee newEmployee = new Employee(null, "Newer", 15, "Female", 8000);
        when(mockedEmployeeRepository.save(newEmployee)).thenReturn(new Employee(6, "Newer", 15, "Female", 8000));

        //when
        Employee returnEmployee = employeeService.save(newEmployee);

        //then
        assertEquals(6, returnEmployee.getId());
        assertEquals(newEmployee.getAge(), returnEmployee.getAge());
        assertEquals(newEmployee.getGender(), returnEmployee.getGender());
        assertEquals(newEmployee.getName(), returnEmployee.getName());
        assertEquals(newEmployee.getSalary(), returnEmployee.getSalary());
    }

    @Test
    void should_return_updated_employee_when_update_employee_give_employee_id_and_target_employee() throws NoSuchDataException, IllegalOperationException {
        //given
        int id = 2;
        Employee beforeUpdateEmployee = new Employee(2, "Spike", 15, "male", 1000);
        Employee afterUpdateEmployee = new Employee(2, "Spike", 20, "male", 10000);
        given(mockedEmployeeRepository.findById(id)).willReturn(Optional.of(beforeUpdateEmployee));
        when(mockedEmployeeRepository.save(beforeUpdateEmployee)).thenReturn(afterUpdateEmployee);

        //when
        Employee updatedEmployee = employeeService.updateEmployee(id, beforeUpdateEmployee);

        //then
        assertEquals(afterUpdateEmployee.getId(), updatedEmployee.getId());
        assertEquals(afterUpdateEmployee.getAge(), updatedEmployee.getAge());
        assertEquals(afterUpdateEmployee.getGender(), updatedEmployee.getGender());
        assertEquals(afterUpdateEmployee.getName(), updatedEmployee.getName());
        assertEquals(afterUpdateEmployee.getSalary(), updatedEmployee.getSalary());
    }

    @Test
    void should_deleteById_run_a_time_when_delete_employee_given_id() throws NoSuchDataException {
        //given
        int id = 6;
        given(mockedEmployeeRepository.findById(id)).willReturn(Optional.of(new Employee(null, "Newer", 15, "Female", 8000)));

        //when
        employeeService.deleteById(id);

        //then
        Mockito.verify(mockedEmployeeRepository).deleteById(id);

    }
//TODO
    @Test
    void should_throw_no_such_data_exception_when_find_by_id_given_not_exist_id() {
        //given
        int notExistId = 5;
        given(mockedEmployeeRepository.findById(notExistId)).willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(NoSuchDataException.class, () -> employeeService.findById(notExistId));

        //then
        assertEquals(NoSuchDataException.class, exception.getClass());
    }

    @Test
    void should_throw_illegal_operation_exception_when_update_employee_given_illegal_id_4_and_employee_id_0() {
        //given
        int illegalId = 4;
        Employee oldEmployee = new Employee(0, "James", 20, "male", 10000);
        //when
        Exception exception = assertThrows(IllegalOperationException.class, () -> employeeService.updateEmployee(illegalId, oldEmployee));

        //then
        assertEquals(IllegalOperationException.class, exception.getClass());
    }

    @Test
    void should_throw_no_such_data_exception_when_update_employee_given_not_exist_id() {
        //given
        int id = 5;
        Employee newEmployee = new Employee(5, "Gavin", 20, "male", 9000);
        given(mockedEmployeeRepository.findById(id)).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(NoSuchDataException.class, () -> employeeService.updateEmployee(id, newEmployee));

        //then
        assertEquals(NoSuchDataException.class, exception.getClass());
    }

    @Test
    void should_throw_no_such_data_exception_when_delete_employee_given_not_exist_id() throws NoSuchDataException {
        //given
        int id = 5;
        Employee newEmployee = new Employee(5, "Gavin", 20, "male", 9000);
        given(mockedEmployeeRepository.findById(id)).willReturn(Optional.empty());

        //when
        Exception exception = assertThrows(NoSuchDataException.class, () -> employeeService.deleteById(id));

        //then
        assertEquals(NoSuchDataException.class, exception.getClass());

    }


}
