package com.report.thewaterstore.store.dto;

import com.report.thewaterstore.store.domain.Store;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class StoreDetailResponseDto {

    private Long id;
    private String name;
    private String description;
    private int level;
    private String address;
    private String phone;
    private List<BusinessDayDto> businessDays;

    public StoreDetailResponseDto() {
    }

    @Builder
    public StoreDetailResponseDto(Long id, String name, String description, int level,
                                  String address, String phone, List<BusinessDayDto> businessDays) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.level = level;
        this.address = address;
        this.phone = phone;
        this.businessDays = businessDays;
    }

    public StoreDetailResponseDto of(Store store, List<BusinessDayDto> businessDayDtoList) {
        return StoreDetailResponseDto.builder()
                .id(store.getId())
                .name(store.getName())
                .description(store.getDescription())
                .address(store.getAddress())
                .level(store.getLevel())
                .phone(store.getPhoneNumber())
                .businessDays(businessDayDtoList)
                .build();
    }
}
