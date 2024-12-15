package com.gebaly.employeemanagement.mapper;

import com.gebaly.employeemanagement.dto.EmployeeDTO;
import com.gebaly.employeemanagement.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(source = "employee.id", target = "id")
    @Mapping(source = "employee.firstName", target = "firstName")
    @Mapping(source = "employee.lastName", target = "lastName")
    @Mapping(source = "employee.email", target = "email")
    @Mapping(source = "employee.department", target = "department")
    @Mapping(source = "employee.salary", target = "salary")
    EmployeeDTO toDto(Employee employee);

    @Mapping(source = "employeeDTO.id", target = "id")
    @Mapping(source = "employeeDTO.firstName", target = "firstName")
    @Mapping(source = "employeeDTO.lastName", target = "lastName")
    @Mapping(source = "employeeDTO.email", target = "email")
    @Mapping(source = "employeeDTO.department", target = "department")
    @Mapping(source = "employeeDTO.salary", target = "salary")
    Employee toEntity(EmployeeDTO employeeDTO);
}
