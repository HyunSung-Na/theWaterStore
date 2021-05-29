package com.report.thewaterstore.store.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BusinessTimeDto {

    private String day;
    private String open;
    private String close;

    @Builder
    public BusinessTimeDto(String day, String open, String close) {
        this.day = day;
        this.open = open;
        this.close = close;
    }
}
