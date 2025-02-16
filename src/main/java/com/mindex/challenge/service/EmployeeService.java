package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

import java.io.IOException;

public interface EmployeeService {
    Employee create(Employee employee);
    Employee read(String id);
    Employee update(Employee employee);
    Employee updateAndPersist(Employee employee) throws IOException;
    ReportingStructure getRS(String id);
    Compensation getCompensation(String id);
}
