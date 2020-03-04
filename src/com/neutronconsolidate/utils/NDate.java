package com.neutronconsolidate.utils;

import java.text.SimpleDateFormat;

public class NDate {
    private String date;
    private String month;
    private String year;
    private String day;
    public NDate(java.util.Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        this.date = dateFormat.format(date);
    }

    public NDate(String date) {
        this.date = date;
        String[] parts = date.split("/");
        this.year = parts[0];
        this.month = parts[1];
        this.day = parts[2];
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getDay() {
        return day;
    }

    @Override
    public String toString() {
        return date;
    }
}
