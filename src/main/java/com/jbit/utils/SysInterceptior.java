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
        //获取session
        Object devuser = request.getSession().getAttribute("devuser");
        if (devuser!=null){
            return  true;
        }
        //如果 session 为空的话 重定向到 登录页面  没登录的话 session 是为空的 因为 session 里面 devuser里面的id为空
        response.sendRedirect("/jsp/devlogin.jsp");
        return false;
    }
}
