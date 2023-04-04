package com.example.mytech.entity;

public enum DayOfWeek {

    MONDAY("THỨ 2"),
    TUESDAY("THỨ 3"),
    WEDNESDAY("THỨ 4"),
    THURSDAY ("THỨ 5"),
    FRIDAY("THỨ 6"),
    SATURDAY("THỨ 7"),
    SUNDAY("CHỦ NHẬT");

    public final String label;

    private DayOfWeek(String label) {
        this.label = label;
    }
}
