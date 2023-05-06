package com.example.mytech.entity;

public enum CA {
    CA_1("8h-10h"),
    CA_2("10h-12h");

    private final String label;

    private CA(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

