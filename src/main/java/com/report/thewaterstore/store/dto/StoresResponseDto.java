package com.report.thewaterstore.store.dto;

import com.report.thewaterstore.store.domain.Store;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StoresResponseDto {

    private String name;
    private String description;
    private int level;
    private String businessStatus;

    public StoresResponseDto() {
    }

    @Builder
    private StoresResponseDto(String name, String description, int level, String businessStatus) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.businessStatus = businessStatus;
    }

    public StoresResponseDto of(Store store, String status) {
        return StoresResponseDto.builder()
                .name(store.getName())
                .description(store.getDescription())
                .level(store.getLevel())
                .businessStatus(status)
                .build();
    }
}
