package com.mindex.challenge.controller;

import com.mindex.challenge.data.*;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.read(id);
    }

    //Endpoint for retrieving an employee's compensation by ID
    @GetMapping("/compensation/{id}")
    public Compensation getCompensation(@PathVariable String id) {
        LOG.debug("Received employee get compensation request for id [{}]", id);

        return employeeService.getCompensation(id);
    }

    //Endpoint for updating compensation of an employee by ID
    @PutMapping("/compensation/{id}")
    public Compensation update(@PathVariable String id, @RequestBody Compensation comp) throws IOException {
        LOG.debug("Received compensation create request for id [{}] and compensation [{}]", id, comp);
        Employee e = employeeService.read(id);
        e.setCompensation(comp);
        employeeService.updateAndPersist(e);
        return comp;
    }

    //Endpoint for retrieving an employee's reporting structure by ID
    @GetMapping("/employeeRS/{id}")
    public ReportingStructure getReportingStructure(@PathVariable String id) {
        LOG.debug("Received employee reporting structure request for id [{}]", id);

        return employeeService.getRS(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }
}
