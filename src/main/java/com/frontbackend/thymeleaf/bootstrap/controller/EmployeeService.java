package com.frontbackend.thymeleaf.bootstrap.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontbackend.thymeleaf.bootstrap.model.ChargeBackFile;
import com.frontbackend.thymeleaf.bootstrap.model.EmployeeComparators;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Column;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Order;
import com.frontbackend.thymeleaf.bootstrap.model.paging.Page;
import com.frontbackend.thymeleaf.bootstrap.model.paging.PageArray;
import com.frontbackend.thymeleaf.bootstrap.model.paging.PagingRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeService {

    private static final Comparator<ChargeBackFile> EMPTY_COMPARATOR = (e1, e2) -> 0;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    public PageArray getEmployeesArray(PagingRequest pagingRequest) {
        pagingRequest.setColumns(Stream.of("name", "position", "office", "start_date", "salary")
                                       .map(Column::new)
                                       .collect(Collectors.toList()));
        Page<ChargeBackFile> employeePage = getEmployees(pagingRequest);

        PageArray pageArray = new PageArray();
        pageArray.setRecordsFiltered(employeePage.getRecordsFiltered());
        pageArray.setRecordsTotal(employeePage.getRecordsTotal());
        pageArray.setDraw(employeePage.getDraw());
        pageArray.setData(employeePage.getData()
                                      .stream()
                                      .map(this::toStringList)
                                      .collect(Collectors.toList()));
        return pageArray;
    }

    private List<String> toStringList(ChargeBackFile employee) {
        return Arrays.asList(employee.getName(), employee.getTotalRecords().toString(), sdf.format(employee.getStartDate()),
                employee.getTotalRejects().toString());
    }

    public Page<ChargeBackFile> getEmployees(PagingRequest pagingRequest) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<ChargeBackFile> employees = objectMapper.readValue(new File("/home/sananthaneni/git/Novation/output/files.json"),
                    new TypeReference<List<ChargeBackFile>>() {
                    });

            return getPage(employees, pagingRequest);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return new Page<>();
    }

    private Page<ChargeBackFile> getPage(List<ChargeBackFile> employees, PagingRequest pagingRequest) {
        List<ChargeBackFile> filtered = employees.stream()
                                           .sorted(sortEmployees(pagingRequest))
                                           .filter(filterEmployees(pagingRequest))
                                           .skip(pagingRequest.getStart())
                                           .limit(pagingRequest.getLength())
                                           .collect(Collectors.toList());

        long count = employees.stream()
                              .filter(filterEmployees(pagingRequest))
                              .count();

        Page<ChargeBackFile> page = new Page<>(filtered);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());

        return page;
    }

    private Predicate<ChargeBackFile> filterEmployees(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                                                                                  .getValue())) {
            return employee -> true;
        }

        String value = pagingRequest.getSearch()
                                    .getValue();

        return employee -> employee.getName()
                                   .toLowerCase()
                                   .contains(value)
                || employee.getTotalRecords().toString()
                          .equals(value)
                          
                || employee.getTotalRejects().toString()
                .equals(value);
    }

    private Comparator<ChargeBackFile> sortEmployees(PagingRequest pagingRequest) {
        if (pagingRequest.getOrder() == null) {
            return EMPTY_COMPARATOR;
        }

        try {
            Order order = pagingRequest.getOrder()
                                       .get(0);

            int columnIndex = order.getColumn();
            Column column = pagingRequest.getColumns()
                                         .get(columnIndex);

            Comparator<ChargeBackFile> comparator = EmployeeComparators.getComparator(column.getData(), order.getDir());
            if (comparator == null) {
                return EMPTY_COMPARATOR;
            }

            return comparator;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return EMPTY_COMPARATOR;
    }
}
