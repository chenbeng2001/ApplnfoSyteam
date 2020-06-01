package com.jbit.entity;

import java.util.HashMap;

/**
 * json 返回对象  就是判断 是否存在输出是信息
 *
 * */
public class JsonResult  extends HashMap<String,Object> {

    public JsonResult message(String message){
        this.put("message",message);
        return this;
    }
    public  JsonResult(boolean success){
        this.put("success",success);

    }

}
