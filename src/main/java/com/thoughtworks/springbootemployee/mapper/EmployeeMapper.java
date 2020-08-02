package com.thoughtworks.springbootemployee.mapper;

import com.thoughtworks.springbootemployee.mapper.request.EmployeeRequest;
import com.thoughtworks.springbootemployee.mapper.response.EmployeeResponse;
import com.thoughtworks.springbootemployee.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public Employee EmployeeRequestToEmployee(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        employee.setId(employeeRequest.getId());
        employee.setAge(employeeRequest.getAge());
        employee.setCompanyId(employeeRequest.getCompanyId());
        employee.setGender(employeeRequest.getGender());
        employee.setName(employeeRequest.getName());
        employee.setSalary(employeeRequest.getSalary());
        return employee;
    }

    public EmployeeResponse EmployeeToEmployeeResponse(Employee employee) {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setId(employee.getId());
        employeeResponse.setAge(employee.getAge());
        employeeResponse.setCompanyId(employee.getCompanyId());
        employeeResponse.setGender(employee.getGender());
        employeeResponse.setName(employee.getName());
        employeeResponse.setSalary(employee.getSalary());
        ///BeanUtils.copyProperties(employee,employeeResponse);
        return employeeResponse;
    }
}
