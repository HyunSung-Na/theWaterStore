package com.report.thewaterstore.store.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StoreHolidayCreateDto {

    private Long id;
    private List<String> holidays;

    public StoreHolidayCreateDto(Long id, List<String> holidays) {
        this.id = id;
        this.holidays = holidays;
    }
}
