package com.example.jwt.config;

import com.example.jwt.config.jwt.JwtAuthenticationFilter;
import com.example.jwt.config.jwt.JwtAuthorizationFilter;
import com.example.jwt.filter.MyFilter1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new MyFilter1(), BasicAuthenticationFilter.class); // BasicAuthenticationFilter가 실행되기 전에 myfilter를 건다
        http.csrf().disable().headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않겠다.
                .and()
                .addFilter(corsFilter)
                .formLogin().disable()
                .httpBasic().disable() // 확인
                .addFilter(new JwtAuthenticationFilter(authenticationManager())) // AuthenticationManager를 파라미터로 넘겨야함
                .addFilter(new JwtAuthorizationFilter(authenticationManager()))
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();

    }
}
