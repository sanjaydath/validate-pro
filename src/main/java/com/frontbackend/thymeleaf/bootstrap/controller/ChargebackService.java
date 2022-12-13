package com.frontbackend.thymeleaf.bootstrap.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontbackend.thymeleaf.bootstrap.model.Chargeback;
import com.frontbackend.thymeleaf.bootstrap.model.Contract;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Page;
import com.frontbackend.thymeleaf.bootstrap.model.paging.PagingRequest;
import com.frontbackend.thymeleaf.bootstrap.rules.ValidateRules;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChargebackService {

    private static final Comparator<Contract> EMPTY_COMPARATOR = (e1, e2) -> 0;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    private ValidateRules rules = new ValidateRules();
    
    private List<String> toStringList(Contract contract) {
        return Arrays.asList(contract.getContractId(), contract.getCustomerID(), contract.getCustomerName(), sdf.format(contract.getStartdate()),
        		contract.getProductId()
                        .toString());
    }
    
    public Page<Chargeback> getChargebacks(PagingRequest pagingRequest) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
			/*
			 * List<Chargeback> employees =
			 * objectMapper.readValue(getClass().getClassLoader()
			 * .getResourceAsStream("charge-backs.json"), new
			 * TypeReference<List<Chargeback>>() { });
			 */
        	
        	List<Chargeback> employees = rules.chargeBackDb.entrySet().stream().map(entry-> entry.getValue()).collect(Collectors.toList());

            return getPage(employees, pagingRequest);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return new Page<>();
    }
    
    private Page<Chargeback> getPage(List<Chargeback> employees, PagingRequest pagingRequest) {
        List<Chargeback> filtered = employees.stream()
//                                           .sorted(sortEmployees(pagingRequest))
                                           .filter(filterEmployees(pagingRequest))
                                           .skip(pagingRequest.getStart())
                                           .limit(pagingRequest.getLength())
                                           .collect(Collectors.toList());

        long count = employees.stream()
                              .filter(filterEmployees(pagingRequest))
                              .count();

        Page<Chargeback> page = new Page<>(filtered);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());

        return page;
    }

    private Predicate<Chargeback> filterEmployees(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                                                                                  .getValue())) {
            return employee -> true;
        }

        String value = pagingRequest.getSearch()
                                    .getValue();

        return employee -> employee.getProductPrice()
                                   .toLowerCase()
                                   .contains(value)
                || employee.getProductId()
                           .toLowerCase()
                           .contains(value)
                || employee.getCustomerID()
                           .toLowerCase()
                           .contains(value);
    }

}
