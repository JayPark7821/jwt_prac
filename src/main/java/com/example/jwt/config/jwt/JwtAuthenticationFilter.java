package com.example.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

// 스프링 시큐리티에 UsernamePasswordAuthenticationFilter 가 있음
// login 요청해서 username,password 전송하면(post)
// UsernamePasswordAuthenticationFilter 동작함
// 하지만 우리가 WebSecurityConfigurerAdapter를 extends 받은 SecurityConfig 에서  .formLogin().disable() 해버림.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 메소드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter.attemptAuthentication");

        // 1. username, password 받아서



        // 3.principalDetails를 세션에 담고
        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println("user = " + user);

            //  jwt 토큰을 만들어서 응답해줌
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // PrincipalDetailsService ->  loadUserByUsername() 메소드 실행됨
            // 2. 정상인지 로그인 시도 ->authenticationManager로 로그인 시도를 하면
            // PrincipalDetailsService 호출 loadUserByUsername() 호출

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("principalDetails = " + principalDetails.getUser().getUsername());
            // Authentication 객체가 session영역에 저장을 해야함 즉 return authentication. => 로그인됨
            // 리턴이유는 권한관리를 security가 대신 해주기때문에
            // jwt는 세션을 만들 필요가 없지만 권한 처리때문에 session생성


            return authentication;

        } catch (IOException e) {
            System.out.println("==========================================");
            throw new RuntimeException(e);
        }
//


    }

    //attemptAuthentication 메소드가 종료되면 그담에 호출되는 함수는
    // successfulAuthentication이 호출
    // 여기서 jwt 토큰을 만들어서 request 요청한 사용자에게 jwt토큰을 response해주면됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("JwtAuthenticationFilter.successfulAuthentication  인증이 완료됨!!!!");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+(60000*10)))// 60000*10
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("cos"));

        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
