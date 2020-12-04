package com.demo.crm.web.filter;

import com.demo.crm.settings.daomian.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest= (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse= (HttpServletResponse) servletResponse;
        String path=httpServletRequest.getServletPath();

        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            User user= (User) httpServletRequest.getSession().getAttribute("user");
            //判断是否有session
            if (user!=null){
                //有就放行
                filterChain.doFilter(servletRequest,servletResponse);

            }else{
//                System.out.println("--------------------------"+httpServletRequest.getContextPath());///crm
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/login.jsp");
            }
        }




    }

    public void destroy() {

    }
    public void init(FilterConfig filterConfig) throws ServletException {

    }

}
