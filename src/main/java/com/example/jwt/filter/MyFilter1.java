package com.example.jwt.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter1 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String headerAuth = req.getHeader("Authorization");
        System.out.println("headerAuth = " + headerAuth);

        if (headerAuth.equals("cos")) {
            System.out.println("인증o");
            chain.doFilter(req, res);
        }else{
//            PrintWriter out = res.getWriter();
//            out.print("인증x");
            chain.doFilter(req, res);
        }


    }
}
