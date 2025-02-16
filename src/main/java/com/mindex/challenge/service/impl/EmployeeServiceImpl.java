package com.mindex.challenge.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private static final String DATASTORE_FULL_LOCATION = "src/main/resources/static/employee_database.json";
    private static final String DATASTORE_LOCATION = "/static/employee_database.json";

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateAndPersist(Employee employee) throws IOException {
        LOG.debug("Updating and persisting employee [{}]", employee);

        //Load the employees into an array from the json file, rather than from the employeeRepository, which
        //may not be up to date with the file due to test classes utilizing it.
        InputStream inputStream = this.getClass().getResourceAsStream(DATASTORE_LOCATION);
        Employee[] employees = null;
        try {
            employees = objectMapper.readValue(inputStream, Employee[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //clear the employee repository of the original employee, before saving the updated employee
        employeeRepository.deleteByEmployeeId(employee.getEmployeeId());
        Employee e = employeeRepository.save(employee);

        //Replace the de-serialized employee object with its updated version
        for(int i = 0; i < employees.length; i++){
            if(employees[i].getEmployeeId().equals(e.getEmployeeId())){
                employees[i] = e;
            }
        }

        //Write the new list of employees to the file
        File f = new File(DATASTORE_FULL_LOCATION);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL).writerWithDefaultPrettyPrinter().writeValues(f).write(Arrays.asList(employees));
        return e;
    }


    @Override
    public ReportingStructure getRS(String id) {
        ReportingStructure rs = new ReportingStructure();
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        rs.setEmployee(employee);
        int reports = 0;
        //Add up the employee's direct reports, and recursively, each direct report's number of reports to get the total number of reports
        if((employee.getDirectReports() != null) && !(employee.getDirectReports().isEmpty())){
            for(Employee e : employee.getDirectReports()){
                reports++;
                reports += getRS(e.getEmployeeId()).getNumberOfReports();
            }
        }
        rs.setNumberOfReports(reports);
        return rs;
    }

    @Override
    public Compensation getCompensation(String id) {
        return read(id).getCompensation();
    }
}
