package com.hz.book.exception;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

/**
 * 异常处理逻辑
 */
@RestControllerAdvice
public class ExceptionController {


    @ExceptionHandler
    public JSONObject handleException(Exception e){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("code","4000004");
        jsonObject.put("msg","发生了未知的错误");
        jsonObject.put("message",e.getMessage());
        //e.printStackTrace();
        return jsonObject;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public JSONObject HttpMediaTypeNotSupportedException(Exception e){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("code","999999");
        jsonObject.put("msg","参数缺失,类型错误");
        jsonObject.put("data","[]");
        return jsonObject;
    }

    @ExceptionHandler(MultipartException.class)
    public JSONObject handleException(MultipartException e){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("code","999999");
        jsonObject.put("msg","参数缺失,类型错误");
        jsonObject.put("data","[]");
        return jsonObject;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public JSONObject handleException(HttpMessageNotReadableException e){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("code","999999");
        jsonObject.put("msg","格式错误,请重新输入");
        jsonObject.put("data","[]");
        return jsonObject;
    }

    @ExceptionHandler(InvalidFormatException.class)
    public JSONObject handleException(InvalidFormatException e){
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("code","999999");
        jsonObject.put("msg","格式错误,请重新输入");
        jsonObject.put("data","[]");
        return jsonObject;
    }


}
