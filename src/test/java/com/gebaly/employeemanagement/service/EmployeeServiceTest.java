package com.gebaly.employeemanagement.service;

import com.gebaly.employeemanagement.dto.EmployeeDTO;
import com.gebaly.employeemanagement.exception.EmployeeNotFoundException;
import com.gebaly.employeemanagement.mapper.EmployeeMapper;
import com.gebaly.employeemanagement.model.Employee;
import com.gebaly.employeemanagement.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Test
    public void testCreateEmployee_valid() {
        // Given
        EmployeeDTO employeeDTO = new EmployeeDTO("John", "Doe", "john.doe@company.com", "Department", 1000.0);
        Employee employee = new Employee(); // Assuming Employee object from employeeDTO details

        // When
        when(employeeMapper.toEntity(employeeDTO)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(employeeDTO);

        // Then
        EmployeeDTO savedEmployeeDTO = employeeService.createEmployee(employeeDTO);

        verify(employeeMapper, times(1)).toEntity(employeeDTO);
        verify(employeeRepository, times(1)).save(employee);
        verify(employeeMapper, times(1)).toDto(employee);
        assertEquals(employeeDTO, savedEmployeeDTO);
    }

    @Test
    public void testGetEmployeeById_exists() {
        // Given
        String Id = UUID.randomUUID().toString();
        Employee employee = new Employee();
        EmployeeDTO employeeDTO = new EmployeeDTO("John", "Doe", "john.doe@company.com", "Department", 1000.0);

        // When
        when(employeeRepository.findById(UUID.fromString(Id))).thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(employee)).thenReturn(employeeDTO);

        // Then
        EmployeeDTO existingEmployeeDTO = employeeService.getEmployeeById(Id);

        verify(employeeRepository, times(1)).findById(UUID.fromString(Id));
        verify(employeeMapper, times(1)).toDto(employee);
        assertEquals(employeeDTO, existingEmployeeDTO);
    }

    @Test
    public void testGetEmployeeById_notExists() {
        // Given
        String Id = UUID.randomUUID().toString();

        // When
        when(employeeRepository.findById(UUID.fromString(Id))).thenReturn(Optional.empty());

        // Then
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(Id));
        verify(employeeRepository, times(1)).findById(UUID.fromString(Id));
    }

    @Test
    public void testUpdateEmployee_valid() {
        String Id = UUID.randomUUID().toString();
        EmployeeDTO employeeDTO = new EmployeeDTO("John", "Doe", "john.doe@updated.com", "Updated Department", 2000.0);
        Employee oldEmployee = new Employee();
        Employee updatedEmployee = new Employee(); // Assuming updated Employee object from employeeDTO details

        // When
        when(employeeRepository.findById(UUID.fromString(Id))).thenReturn(Optional.of(oldEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee); // Modify this line
        when(employeeMapper.toDto(updatedEmployee)).thenReturn(employeeDTO);

        // Then
        EmployeeDTO updatedEmployeeDTO = employeeService.updateEmployee(Id, employeeDTO);

        verify(employeeRepository, times(1)).findById(UUID.fromString(Id));
        verify(employeeRepository, times(1)).save(any(Employee.class)); // And this line
        verify(employeeMapper, times(1)).toDto(updatedEmployee);
        assertEquals(employeeDTO, updatedEmployeeDTO);
    }

    @Test
    public void testUpdateEmployee_notExists() {
        // Given
        String Id = UUID.randomUUID().toString();
        EmployeeDTO employeeDTO = new EmployeeDTO("John", "Doe", "john.doe@updated.com", "Updated Department", 2000.0);

        // When
        when(employeeRepository.findById(UUID.fromString(Id))).thenReturn(Optional.empty());

        // Then
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(Id, employeeDTO));
        verify(employeeRepository, times(1)).findById(UUID.fromString(Id));
    }
}