package com.jbit.web;


import com.jbit.pojo.BackendUser;
import com.jbit.service.BackUserServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("backend") //表示前缀为  back
public class BackUserController {


    @Resource
    private BackUserServer backUserServer;


    @PostMapping("login")
    public String login(String usercode, String userpassword, HttpSession session){
        BackendUser backendUser = backUserServer.querylogin(usercode, userpassword);
        if (backendUser!=null){
             session.setAttribute( "backuser",backendUser);
            return "redirect:/jsp/backend/main.jsp";
        }

        return "backendlogin";
    }
    /**
     *  退出
     *
     */
    @GetMapping("logout")
    public String logout(HttpSession session){
        //清空session 退出
        session.invalidate();
        return "backendlogin";

    }

}
