package com.example.jwt.config;

import com.example.jwt.filter.MyFilter1;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1() {
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<MyFilter1>(new MyFilter1());
        bean.addUrlPatterns("/*");
        bean.setOrder(0);// 낮은번호가 가장먼저 실행됨
        return bean;
    }
}
