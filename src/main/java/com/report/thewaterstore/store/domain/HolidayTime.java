package com.report.thewaterstore.store.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class HolidayTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String holidayTime;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "store_id")
    private Store store;

    public HolidayTime() {
    }

    public HolidayTime(String holidayTime, Store store) {
        this.holidayTime = holidayTime;
        this.store = store;
    }

    public HolidayTime(Long id, String holidayTime, Store store) {
        this.id = id;
        this.holidayTime = holidayTime;
        this.store = store;
    }
}
