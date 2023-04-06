package com.example.mytech.entity;

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
}
