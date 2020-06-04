package com.jbit.utils;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 登录拦截器  实现 HandlerInterceptor 接口
 *
 * */
public class SysInterceptior implements HandlerInterceptor {
    /**
     * 进入请求方法之前实现    true表示请求通过 false 请求失败
     *
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取session  判断如果session 为空的话 就跳转在登录页面
        // 除了 登录不被 springmvc-servlet.xml 连接器拦截 如果跳转到其他页面 的时候session 为空的话 就会放回登录页面
//        System.out.println(request.getMethod());  取出来的是  get 或 post 方法
//        System.out.println(request.getRequestURI()); 取出来的是 /backuser/dev/user
//        System.out.println(request.getRequestURL());   取出来的是 http://localhost:8080/jsp/devlogin.jsp


        Object devuser = request.getSession().getAttribute("devuser");
        Object backuser = request.getSession().getAttribute("backuser");
        if (devuser!=null||backuser!=null){
            return  true;
        }
        //如果 session 为空的话 重定向到 登录页面  没登录的话 session 是为空的 因为 session 里面 devuser里面的id为空
        response.sendRedirect("index.jsp");
        return false;
    }
}
