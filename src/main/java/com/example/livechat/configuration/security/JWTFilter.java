package com.example.livechat.configuration.security;

import com.example.livechat.dao.MyUserDetails;
import com.example.livechat.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    public JWTFilter(JWTUtil jwtUtil) {this.jwtUtil=jwtUtil;}

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization=request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
           filterChain.doFilter(request,response);
           return;
        }
        //Bearer 제거하고 순수 토큰만 획득
        String token = authorization.split(" ")[1];
        if (jwtUtil.isExpired(token)) {
            throw new BadCredentialsException("만료된 토큰입니다.");
        }
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("temp");
        userEntity.setRole(role);

        MyUserDetails myUserDetails = new MyUserDetails(userEntity);
        Authentication autoken = new UsernamePasswordAuthenticationToken(
                myUserDetails,null,myUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(autoken);
        //SecurityFilterChain의 다음 필터로 요청을 전달함
        filterChain.doFilter(request, response);
        response.setHeader("Authorization", "Bearer " + token);
    }
}
