package com.grandata.www.grandc.home.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class BaseInterceptor implements HandlerInterceptor {

    /**
     * 在DispatcherServlet之前执行
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("************BaseInterceptor preHandle executed**********");
        //获取调用方法
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        System.out.println(handlerMethod.getMethod());
        return true;
    }

    /**
     * 在controller执行之后的DispatcherServlet之后执行
     * */
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        System.out.println("************BaseInterceptor postHandle executed**********");
    }

    /**
     * 在页面渲染完成返回给客户端之前执行
     * */
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        System.out.println("************BaseInterceptor afterCompletion executed**********");
    }

}
