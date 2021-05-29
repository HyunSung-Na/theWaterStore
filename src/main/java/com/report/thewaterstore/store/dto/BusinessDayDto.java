package com.report.thewaterstore.store.dto;

import lombok.Getter;

@Getter
public class BusinessDayDto {

    private String day;
    private String open;
    private String close;
    private String status;

    public BusinessDayDto(String day, String open, String close, String status) {
        this.day = day;
        this.open = open;
        this.close = close;
        this.status = status;
    }
}
