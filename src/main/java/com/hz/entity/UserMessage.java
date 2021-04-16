package com.hz.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


@ApiModel(value = "UserMessage对象", description = "返回用户详细信息")
public class UserMessage implements Serializable {
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户全名")
    //@NotEmpty(message = "ID不能为空")
    private String fullName;

    public UserMessage() {
    }

    public UserMessage(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
