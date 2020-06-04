package com.jbit.service;


import com.jbit.mapper.BackendUserMapper;
import com.jbit.pojo.BackendUser;
import com.jbit.utils.AppUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BackUserServer {

    @Resource
    private BackendUserMapper backendUserMapper;

    @Resource
    private  DataDictionaryService dataDictionaryService;


    /**
     * 用户登录
     *
     * */
    public BackendUser querylogin(String userCode,String userpassword){
        BackendUser backendUser=new BackendUser();
        backendUser.setUsercode(userCode);
        //cmd5  密码验证
        backendUser.setUserpassword(AppUtils.encoderByMd5(userpassword));
        BackendUser backeUser = backendUserMapper.selectOne(backendUser);
        backeUser.setUsertypename(dataDictionaryService.querydataDictionary("USER_TYPE",backeUser.getUsertype()).getValuename());

        return backeUser;
    }




}
