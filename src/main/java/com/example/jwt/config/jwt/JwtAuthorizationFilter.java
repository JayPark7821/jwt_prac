package com.example.jwt.config.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// security가 filter를 가지고 있는데 그 필터중 BasicAuthenticationFilter라는 것이 있음
// 권한이나 인증이 필요한 특정 주소를 요청했을때 위 필터를 무조건 타게 되어있음
// 만약 권한이나 인증이 필요한 주소가 아니라면 이 필터를 안탐
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    // 인증이나 권한이 필요한 주소요청이 있을때 해당 필터를 타게됨

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("인증이나 권한이 필요한 주소 요청!!!!!!");
        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader = " + jwtHeader);

        // jwt 토큰을 검증해서 정상적인 사용자인지 확인

        super.doFilterInternal(request, response, chain);
    }
}
