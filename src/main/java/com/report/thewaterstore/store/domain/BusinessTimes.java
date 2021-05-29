package com.report.thewaterstore.store.domain;

import com.report.thewaterstore.error.NotFoundException;
import com.report.thewaterstore.store.dto.BusinessTimeDto;
import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
@Getter
public class BusinessTimes {

    @OneToMany(mappedBy = "store", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private final List<BusinessTime> businessTimes = new ArrayList<>();

    public BusinessTimes() {
    }

    public BusinessTime getBusinessTime(Day day) {
        Optional<BusinessTime> businessTime = this.getBusinessTimes().stream()
                .filter(time -> {
                    return time.getDay().equals(day.getDayOfWeek());
                })
                .findFirst();

        if (businessTime.isEmpty()) {
            return new BusinessTime(day.getDayOfWeek(), "25:00", "26:00", this.getBusinessTimes().get(0).getStore());
        }

        return businessTime.orElseThrow(() -> new NotFoundException(businessTime));
    }

    public void addBusinessTimes(List<BusinessTimeDto> businessTimesDto, Store store) {
        businessTimesDto.forEach(
                it -> {
                    BusinessTime newBusinessTime = new BusinessTime(it.getDay(), it.getOpen(), it.getClose(), store);
                    this.businessTimes.add(newBusinessTime);
                });
    }

    public String isBusiness(Day day) {
        String status = BusinessStatus.OPEN.name();

        if (day.getDayOfWeek().equals("Saturday") || day.getDayOfWeek().equals("Sunday")) {
            return BusinessStatus.CLOSE.name();
        }

        BusinessTime businessTime = this.getBusinessTimes().stream()
                .filter(it -> it.getDay().equals(day.getDayOfWeek()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(day.getDayOfWeek()));

        int openTime = businessTime.openHour();
        int closeTime = businessTime.closeHour();

        if (day.getHour() < openTime || day.getHour() > closeTime) {
            return BusinessStatus.CLOSE.name();
        }

        return status;
    }
}
