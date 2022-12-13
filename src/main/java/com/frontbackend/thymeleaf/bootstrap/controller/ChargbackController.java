package com.frontbackend.thymeleaf.bootstrap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frontbackend.thymeleaf.bootstrap.model.Chargeback;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Page;
import com.frontbackend.thymeleaf.bootstrap.model.paging.PagingRequest;

@RestController

@RequestMapping("itemData")
public class ChargbackController {

	private final ChargebackService chargebackService;

	@Autowired
	public ChargbackController(ChargebackService chargebackService) {
		this.chargebackService = chargebackService;
	}

	@PostMapping
	public Page<Chargeback> list(@RequestBody PagingRequest pagingRequest) {
		
		return chargebackService.getChargebacks(pagingRequest);
	}


}
