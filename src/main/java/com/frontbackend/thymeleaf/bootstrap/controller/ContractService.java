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
import com.frontbackend.thymeleaf.bootstrap.model.Contract;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Page;
import com.frontbackend.thymeleaf.bootstrap.model.paging.PagingRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContractService {

    private static final Comparator<Contract> EMPTY_COMPARATOR = (e1, e2) -> 0;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

	/*
	 * public PageArray getEmployeesArray(PagingRequest pagingRequest) {
	 * pagingRequest.setColumns(Stream.of("name", "position", "office",
	 * "start_date", "salary") .map(Column::new) .collect(Collectors.toList()));
	 * Page<Contract> employeePage = getEmployees(pagingRequest);
	 * 
	 * PageArray pageArray = new PageArray();
	 * pageArray.setRecordsFiltered(employeePage.getRecordsFiltered());
	 * pageArray.setRecordsTotal(employeePage.getRecordsTotal());
	 * pageArray.setDraw(employeePage.getDraw());
	 * pageArray.setData(employeePage.getData() .stream() .map(this::toStringList)
	 * .collect(Collectors.toList())); return pageArray; }
	 */

    private List<String> toStringList(Contract contract) {
        return Arrays.asList(contract.getContractId(), contract.getCustomerID(), contract.getCustomerName(), sdf.format(contract.getStartdate()),
        		contract.getProductId()
                        .toString());
    }

    public Page<Contract> getEmployees(PagingRequest pagingRequest) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Contract> employees = objectMapper.readValue(getClass().getClassLoader()
                                                                        .getResourceAsStream("bid-award-full.json"),
                    new TypeReference<List<Contract>>() {
                    });

            return getPage(employees, pagingRequest);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return new Page<>();
    }
    
    
    public Page<Contract> getContracts(PagingRequest pagingRequest) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Contract> employees = objectMapper.readValue(getClass().getClassLoader()
                                                                        .getResourceAsStream("bid-award-full.json"),
                    new TypeReference<List<Contract>>() {
                    });

            return getPage(employees, pagingRequest);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return new Page<>();
    }

    private Page<Contract> getPage(List<Contract> employees, PagingRequest pagingRequest) {
        List<Contract> filtered = employees.stream()
//                                           .sorted(sortEmployees(pagingRequest))
                                           .filter(filterEmployees(pagingRequest))
                                           .skip(pagingRequest.getStart())
                                           .limit(pagingRequest.getLength())
                                           .collect(Collectors.toList());

        long count = employees.stream()
                              .filter(filterEmployees(pagingRequest))
                              .count();

        Page<Contract> page = new Page<>(filtered);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());

        return page;
    }

    private Predicate<Contract> filterEmployees(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                                                                                  .getValue())) {
            return employee -> true;
        }

        String value = pagingRequest.getSearch()
                                    .getValue();

        return employee -> employee.getContractId()
                                   .toLowerCase()
                                   .contains(value)
                || employee.getProductId()
                           .toLowerCase()
                           .contains(value)
                || employee.getCustomerID()
                           .toLowerCase()
                           .contains(value);
    }

	/*
	 * private Comparator<Contract> sortEmployees(PagingRequest pagingRequest) { if
	 * (pagingRequest.getOrder() == null) { return EMPTY_COMPARATOR; }
	 * 
	 * try { Order order = pagingRequest.getOrder() .get(0);
	 * 
	 * int columnIndex = order.getColumn(); Column column =
	 * pagingRequest.getColumns() .get(columnIndex);
	 * 
	 * Comparator<Contract> comparator =
	 * EmployeeComparators.getComparator(column.getData(), order.getDir()); if
	 * (comparator == null) { return EMPTY_COMPARATOR; }
	 * 
	 * return comparator;
	 * 
	 * } catch (Exception e) { log.error(e.getMessage(), e); }
	 * 
	 * return EMPTY_COMPARATOR; }
	 */
}
