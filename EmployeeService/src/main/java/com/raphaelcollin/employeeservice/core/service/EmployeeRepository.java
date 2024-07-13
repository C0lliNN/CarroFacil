package com.raphaelcollin.employeeservice.core.service;

import com.raphaelcollin.employeeservice.core.Employee;

import java.util.Optional;

public interface EmployeeRepository {
    Employee save(Employee customer);
    Optional<Employee> findByUserId(String userId);
}
