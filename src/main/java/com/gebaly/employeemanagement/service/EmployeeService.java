package com.gebaly.employeemanagement.service;

import com.gebaly.employeemanagement.dto.EmployeeDTO;
import com.gebaly.employeemanagement.exception.EmployeeNotFoundException;
import com.gebaly.employeemanagement.mapper.EmployeeMapper;
import com.gebaly.employeemanagement.model.Employee;
import com.gebaly.employeemanagement.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmailService emailService;


    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = convertToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        // Email notification
        String to = "recipient@example.com"; // Replace with real recipient's email address
        String subject = "New Employee Created";
        String body = "New employee, " + employee.getFirstName() + " " + employee.getLastName() + " is created.";
        emailService.sendEmail(to, subject, body);
        return convertToDto(savedEmployee);
    }


    public EmployeeDTO getEmployeeById(String id) {
        Employee employee = employeeRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EmployeeNotFoundException(UUID.fromString(id)));
        return convertToDto(employee);
    }


    public EmployeeDTO updateEmployee(String id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EmployeeNotFoundException(UUID.fromString(id)));
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setDepartment(employeeDTO.getDepartment());
        employee.setSalary(employeeDTO.getSalary());
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDto(savedEmployee);
    }


    public void deleteEmployee(String id) {
        Employee employee = employeeRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EmployeeNotFoundException(UUID.fromString(id)));
        employeeRepository.delete(employee);
    }


    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private EmployeeDTO convertToDto(Employee employee) {
        return employeeMapper.toDto(employee);
    }


    private Employee convertToEntity(EmployeeDTO employeeDTO) {
        return employeeMapper.toEntity(employeeDTO);
    }
}
