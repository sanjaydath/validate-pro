package com.frontbackend.thymeleaf.bootstrap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frontbackend.thymeleaf.bootstrap.model.ChargeBackFile;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Page;
import com.frontbackend.thymeleaf.bootstrap.model.paging.PageArray;
import com.frontbackend.thymeleaf.bootstrap.model.paging.PagingRequest;

@RestController

@RequestMapping("chargeBacks")
public class ChargeBackFileController {

	private final EmployeeService employeeService;

	@Autowired
	public ChargeBackFileController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PostMapping
	public Page<ChargeBackFile> list(@RequestBody PagingRequest pagingRequest) {
		return employeeService.getEmployees(pagingRequest);
	}

	@PostMapping("/array")
	public PageArray array(@RequestBody PagingRequest pagingRequest) {
		return employeeService.getEmployeesArray(pagingRequest);
	}
}
