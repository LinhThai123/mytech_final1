package com.example.mytech.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ChangeStatusReq {

    private String id ;

    private String user_id;

    private String course_id ;

    private Timestamp enrollDate ;

    private int status ;
}
