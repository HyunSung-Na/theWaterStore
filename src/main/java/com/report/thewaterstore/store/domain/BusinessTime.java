package com.report.thewaterstore.store.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class BusinessTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;
    private String open;
    private String close;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "store_id")
    private Store store;

    public BusinessTime() {
    }

    public BusinessTime(String day, String open, String close, Store store) {
        this.day = day;
        this.open = open;
        this.close = close;
        this.store = store;

        if (openHour() == closeHour()) {
            throw new RuntimeException("오픈시간과 마감시간이 같습니다.");
        }
    }

    public BusinessTime(Long id, String day, String open, String close, Store store) {
        this.id = id;
        this.day = day;
        this.open = open;
        this.close = close;
        this.store = store;
    }

    public int openHour() {
        String[] split = this.getOpen().split(":");
        return Integer.parseInt(split[0]);
    }

    public int closeHour() {
        String[] split = this.getClose().split(":");
        return Integer.parseInt(split[0]);
    }
}
