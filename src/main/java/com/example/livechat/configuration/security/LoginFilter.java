package com.example.livechat.configuration.security;

import com.example.livechat.dao.MyUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager,JWTUtil jwtUtil) {
        this.authenticationManager=authenticationManager;
        this.jwtUtil = jwtUtil;

    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
       //필터가 요청에서 username과 비번 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
      //authenticationManager에게 토큰 전달
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        // 기존 JWT 토큰 삭제
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    cookie.setMaxAge(0); // 쿠키 만료 설정
                    cookie.setPath("/"); // 쿠키 경로 설정
                    response.addCookie(cookie); // 응답에 쿠키 추가 (삭제)
                    break;
                }
            }
        }

        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        String username = myUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        String token = jwtUtil.createJwt(username,role,60*60*10L);

        Cookie jwtCookie = new Cookie("jwtToken", token);
        jwtCookie.setHttpOnly(true); // JavaScript를 통한 접근 방지
        jwtCookie.setPath("/"); // 쿠키의 경로 설정
        response.addCookie(jwtCookie); // 응답에 쿠키 추가


        response.addHeader("Authorization","Bearer "+token);

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String jsonResponse = String.format("{\"authToken\": \"%s\"}", token);
        out.print(jsonResponse);
        out.flush();

       // mySuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) {
        logger.debug("LoginFilter : 인증에 실패했습니다. " + e.getMessage());
        response.setStatus(401);
    }
}
