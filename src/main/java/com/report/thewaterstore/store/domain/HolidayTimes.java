package com.report.thewaterstore.store.domain;

import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
public class HolidayTimes {

    @OneToMany(mappedBy = "store", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<HolidayTime> holidays = new ArrayList<>();

    public HolidayTimes() {
    }

    public void addHolidayTimes(List<String> holidays, Store store) {
        holidays.forEach(
                it -> {
                    HolidayTime newHolidayTime = new HolidayTime(it, store);
                    this.holidays.add(newHolidayTime);
                });
    }

    public String isHoliday(Day day) {
        String today = day.formatYYYYMMDD();

        if (matchDay(today)) {
            return BusinessStatus.HOLIDAY.name();
        }

        return "NO";
    }

    private boolean matchDay(String today) {
        return this.getHolidays().stream().anyMatch(it ->
                it.getHolidayTime().equals(today));
    }
}
