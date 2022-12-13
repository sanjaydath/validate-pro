package com.frontbackend.thymeleaf.bootstrap.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChargeBackFile {

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date startDate;
    private Integer id;
    private String name;
    private Long totalRecords;
    private Long totalRejects; 

}
