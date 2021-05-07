package com.hz.demo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


@ApiModel(value = "ConvertResult对象", description = "返回结果详情")
public class ConvertResult implements Serializable {

    @ApiModelProperty(value = "状态码,100000=FAIL,0=SUCCESS,210007=TOKEN_ERROR,110001=PARAMETER_IS_NOT_VALID,110002=DOCUMENT_IS_NOT_EXIST,110006=IS_NOT_OWNER,21000")
    private Integer code;

    @ApiModelProperty(value = "返回信息")
    private String message;

    @ApiModelProperty(value = "结果")
    private Object result;


    public ConvertResult() {
    }

    public ConvertResult(Integer code, String message, Object result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ConvertResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
