package com.report.thewaterstore.store.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class StoreCreateDto {

    private String name;
    private String owner;
    private String description;
    private int level;
    private String address;
    private String phoneNumber;
    private List<BusinessTimeDto> businessTimes;

    @Builder
    public StoreCreateDto(String name, String owner, String description, int level,
                          String address, String phoneNumber, List<BusinessTimeDto> businessTimes) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.level = level;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.businessTimes = businessTimes;
    }
}
