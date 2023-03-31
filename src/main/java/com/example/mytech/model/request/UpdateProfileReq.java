package com.example.mytech.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UpdateProfileReq {
    private String id ;
    private String name ;
    private String address;
    private String phone ;
}
