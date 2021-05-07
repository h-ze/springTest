package com.hz.demo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor                 //无参构造
@AllArgsConstructor                //有参构造
public class Email {
    private Integer emailId;
    private String email;
    private Integer status;
}
