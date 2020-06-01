package com.jbit.web;

import com.jbit.pojo.DevUser;
import com.jbit.service.DevUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("dev") //表示     区分前缀        如果跳转是login的话 需要加dev标识
public class DevUserController {

     @Resource
     private DevUserService devUserService;

    /**
     * 开发者登录
     *
     * */
    @PostMapping("login")
      public String login(Model model, HttpSession session, String devcode, String devpassword){
          DevUser devUser = devUserService.queryLogin(devcode, devpassword);
          if (devUser!=null){
              session.setAttribute("devuser",devUser);
              return "redirect:/jsp/developer/main.jsp";          //使用重定向
          }
            model.addAttribute("error","账号密码不正确");
          return "devlogin";
      }

//     用户退出
    @GetMapping("logout")
    public String logout(HttpSession session){
       session.invalidate();      //清楚session里面的数据
        return "redirect:/jsp/devlogin.jsp";
    }
}
