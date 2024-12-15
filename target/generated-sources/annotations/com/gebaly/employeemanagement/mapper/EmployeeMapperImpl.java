package com.gebaly.employeemanagement.mapper;

import com.gebaly.employeemanagement.dto.EmployeeDTO;
import com.gebaly.employeemanagement.model.Employee;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T22:14:11+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeDTO toDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        String firstName = null;
        String lastName = null;
        String email = null;
        String department = null;
        Double salary = null;

        EmployeeDTO employeeDTO = new EmployeeDTO( firstName, lastName, email, department, salary );

        return employeeDTO;
    }

    @Override
    public Employee toEntity(EmployeeDTO employeeDTO) {
        if ( employeeDTO == null ) {
            return null;
        }

        Employee employee = new Employee();

        return employee;
    }
}
