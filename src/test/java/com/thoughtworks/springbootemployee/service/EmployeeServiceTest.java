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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
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
    void should_return_all_employees_when_get_all_employee_given_no_parameter() {
        //given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(0, "James", 20, "Male", 10000));
        employees.add(new Employee(1, "Penny", 19, "Female", 10000));
        employees.add(new Employee(2, "Spike", 15, "Male", 10000));
        when(mockedEmployeeRepository.findAll()).thenReturn(employees);

        //when
        List<Employee> foundEmployees = employeeService.findAll();

        //then
        assertEquals(3, foundEmployees.size());
    }

    @Test
    void should_return_Xiaoming_when_get_employee_by_id_given_0() throws NoSuchDataException {
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
        List<Employee> employees = new LinkedList<>();
        employees.add(new Employee(2, "Xiaozhi", 15, "Male", 10000));
        employees.add(new Employee(3, "Xiaogang", 16, "Male", 10000));
        given(mockedEmployeeRepository.findAll(PageRequest.of(2, 2)))
                .willReturn(new PageImpl<>(employees));

        //when
        List<Employee> resultEmployees = employeeService.findAll(page, pageSize).getContent();

        //then
        Mockito.verify(mockedEmployeeRepository).findAll(PageRequest.of(page, pageSize));
        assertEquals(2, resultEmployees.get(0).getId());
        assertEquals(3, resultEmployees.get(1).getId());

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

    @Test
    void should_return_updated_employee_when_update_employee_give_employee_id_and_target_employee() throws NoSuchDataException, IllegalOperationException {
        //given
        int id = 1;
        Employee targetEmployee = new Employee(1, "Xiaohong1", 20, "Male", 9000);
        when(mockedEmployeeRepository.findById(id)).thenReturn(generateEmployees().stream().filter(employee -> employee.getId() == id).findFirst());
        when(mockedEmployeeRepository.save(targetEmployee)).thenReturn(targetEmployee);

        //when
        Employee updatedEmployee = employeeService.updateEmployee(id, targetEmployee);

        //then
        assertEquals(targetEmployee.getId(), updatedEmployee.getId());
        assertEquals(targetEmployee.getAge(), updatedEmployee.getAge());
        assertEquals(targetEmployee.getGender(), updatedEmployee.getGender());
        assertEquals(targetEmployee.getName(), updatedEmployee.getName());
        assertEquals(targetEmployee.getSalary(), updatedEmployee.getSalary());
    }

    @Test
    void should_return_boolean_when_delete_employee_given_id() {
        //given
        int id = 3;
        when(mockedEmployeeRepository.findById(id)).thenReturn((new ArrayList<Employee>()).stream().filter(employee -> employee.getId() == id).findFirst());

        //when
        boolean isDelete = employeeService.deleteById(id);

        //then
        assertTrue(isDelete);
        Mockito.verify(mockedEmployeeRepository).deleteById(id);

    }

    @Test
    void should_throw_no_such_data_exception_when_find_by_id_given_not_exist_id() {
        //given
        int notExistId = 5;

        given(mockedEmployeeRepository.findById(notExistId)).willReturn(Optional.of(null));

        // when
        Exception exception = assertThrows(NoSuchDataException.class, () -> employeeService.findById(notExistId));

        //then
        assertEquals(NoSuchDataException.class, exception.getClass());
    }

    @Test
    void should_throw_illegal_operation_exception_when_update_employee_given_illegal_id_4_and_employee_id_3() {
        //given
        int illegalId = 4;
        Employee employee = generateEmployees().get(3);

        //when
        Exception exception = assertThrows(IllegalOperationException.class, () -> employeeService.updateEmployee(illegalId, employee));

        //then
        assertEquals(IllegalOperationException.class, exception.getClass());
    }

    @Test
    void should_throw_no_such_data_exception_when_update_employee_given_not_exist_id() {
        //given
        int id = 5;
        Employee employee = new Employee(5, "123", 1, "female", 1000);
        given(mockedEmployeeRepository.findById(id)).willReturn(null);

        //when
        Exception exception = assertThrows(NoSuchDataException.class, () -> employeeService.updateEmployee(id, employee));

        //then
        assertEquals(NoSuchDataException.class, exception.getClass());
    }
}
