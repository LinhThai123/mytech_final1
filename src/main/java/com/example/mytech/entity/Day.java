package com.example.mytech.entity;

import java.time.DayOfWeek;

public enum Day {
    MONDAY("Thứ 2"),
    TUESDAY("Thứ 3"),
    WEDNESDAY("Thứ 4"),
    THURDAY("Thứ 5"),
    FRIDAY("Thứ 6"),
    SATURDAY("Thứ 7"),
    SUNDAY("Chủ nhật");

    public final String label;

    private Day(String label) {
        this.label = label;
    }

    public DayOfWeek toDayOfWeek() {
        return DayOfWeek.valueOf(this.name());
    }
    public String getLabel() {
        return label;
    }
}
