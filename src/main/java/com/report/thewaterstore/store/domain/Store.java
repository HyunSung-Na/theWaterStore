package com.report.thewaterstore.store.domain;

import com.report.thewaterstore.store.dto.BusinessDayDto;
import com.report.thewaterstore.store.dto.BusinessTimeDto;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String owner;
    private String description;
    private int level;
    private String address;
    private String phoneNumber;

    @Embedded
    private final BusinessTimes businessTimes = new BusinessTimes();

    @Embedded
    private final HolidayTimes holidayTimes = new HolidayTimes();

    public Store() {
    }

    @Builder
    public Store(String name, String owner, String description, int level, String address, String phoneNumber) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.level = level;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    @Builder
    public Store(Long id, String name, String owner, String description, int level, String address, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.level = level;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public void addBusinessTime(List<BusinessTimeDto> businessTimesDto) {
        this.getBusinessTimes().addBusinessTimes(businessTimesDto, this);
    }

    public void addHolidays(List<String> holidays) {
        this.getHolidayTimes().addHolidayTimes(holidays, this);
    }

    public String isHoliday(Day day) {
        return this.getHolidayTimes().isHoliday(day);
    }

    public String isBusiness(Day day) {
        return this.getBusinessTimes().isBusiness(day);
    }

    public List<BusinessDayDto> detailBusinessDays(Day day) {
        List<BusinessDayDto> businessDayDtoList = new ArrayList<>();
        for (int index = 0; index < 3; index++) {
            Day newDay = day;
            String status = isHoliday(day);

            if (status.equals("NO")) {
                status = isBusiness(day);
            }

            BusinessTime businessTime = this.getBusinessTimes().getBusinessTime(day);

            if (businessTime.getOpen().equals("25:00")) {
                status = BusinessStatus.HOLIDAY.name();
            }

            businessDayDtoList.add(new BusinessDayDto(
                    businessTime.getDay(),
                    businessTime.getOpen(),
                    businessTime.getClose(),
                    status));

            day = day.tomorrow();
        }

        return businessDayDtoList;
    }
}
