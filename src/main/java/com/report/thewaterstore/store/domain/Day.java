package com.report.thewaterstore.store.domain;

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Getter
public class Day {

    private Calendar cal;
    private String dayOfWeek;
    private int hour;
    private int date;
    private int year;
    private int month;

    public Day(Calendar calendar) {
        this.cal = calendar;
        this.hour = cal.get(Calendar.HOUR);
        this.date = cal.get(Calendar.DATE);
        this.month = cal.get(Calendar.MONTH) + 1;
        this.year = cal.get(Calendar.YEAR);
        transDayWeek(cal.get(Calendar.DAY_OF_WEEK));
    }

    public String formatYYYYMMDD() {
        Date newDate = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(newDate);
    }

    private void transDayWeek(int dayOfNumber) {
        switch (dayOfNumber) {
            case 1:
                dayOfWeek = "Sunday";
                break;
            case 2:
                dayOfWeek = "Monday";
                break;
            case 3:
                dayOfWeek = "Tuesday";
                break;
            case 4:
                dayOfWeek = "Wednesday";
                break;
            case 5:
                dayOfWeek = "Thursday";
                break;
            case 6:
                dayOfWeek = "Friday";
                break;
            case 7:
                dayOfWeek = "Saturday";
                break;
        }
    }

    public Day tomorrow() {
        cal.add(Calendar.DATE, 1);
        this.hour = cal.get(Calendar.HOUR);
        this.date = cal.get(Calendar.DATE);
        this.month = cal.get(Calendar.MONTH) + 1;
        this.year = cal.get(Calendar.YEAR);
        transDayWeek(cal.get(Calendar.DAY_OF_WEEK));
        return this;
    }
}
