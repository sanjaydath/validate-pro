package com.frontbackend.thymeleaf.bootstrap.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Chargeback {
	
	public String  ContractId;
	public String  CustomerID;
	public String  ProductId;
	public String  ProductPrice;
	public String  Startdate;
	public String  rejectReasons;
	
	
}
