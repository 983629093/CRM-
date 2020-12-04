package com.demo.crm.web.filter;


import javax.servlet.*;
import java.io.IOException;


public  class EncodingFilter implements  Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        //post请求参数乱码
        servletRequest.setCharacterEncoding("utf-8");
        //响应体参数乱码
        servletResponse.setContentType("text/html;charset=utf-8");
        filterChain.doFilter(servletRequest,servletResponse);


    }

    public void destroy() {

    }


}
