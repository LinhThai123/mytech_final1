package com.example.mytech.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AttendanceDTO {

    private String userID ;

    private String scheduleID;

    private boolean attendance ;

    public AttendanceDTO(String userID, boolean attendance) {
        this.userID = userID;
        this.attendance = attendance;
    }

}
