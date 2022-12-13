package com.frontbackend.thymeleaf.bootstrap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.frontbackend.thymeleaf.bootstrap.model.Contract;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Page;
import com.frontbackend.thymeleaf.bootstrap.model.paging.PagingRequest;

@RestController

@RequestMapping("contracts")
public class ContractsController {

	private final ContractService contractService;

	@Autowired public ContractsController(ContractService contractService) {
  this.contractService = contractService; }

	@PostMapping
	public Page<Contract> list(@RequestBody PagingRequest pagingRequest) {
		return contractService.getContracts(pagingRequest);
	}

	/*
	 * @PostMapping("/array") public PageArray array(@RequestBody PagingRequest
	 * pagingRequest) { return contractService.getEmployeesArray(pagingRequest); }
	 */
	
	/*
	 * void processBidAwards(File file) throws IOException {
	 * 
	 * ObjectMapper mapper = new ObjectMapper();
	 * 
	 * List<Contract> list = new ArrayList();
	 * 
	 * list.add(mapper.readValue(file, Contract.class));
	 * 
	 * Map<String, Contract> map =
	 * 
	 * list.stream().collect(Collectors.toMap(x -> x.getContractId(), x -> x));
	 * 
	 * bidAwardsDb.putAll(map);
	 * 
	 * }
	 */
}
