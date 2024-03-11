package com.example.livechat.Configuration;

import com.example.livechat.DAO.MyUserDetails;
import com.example.livechat.Entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        if (authorization != null || !authorization.startsWith("Bearer ")) {
           filterChain.doFilter(request,response);
           return;
        }
        //Bearer 제거하고 순수 토큰만 획득
        String token = authorization.split(" ")[1];
        if (jwtUtil.isExpired(token)) {
            return;
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
        filterChain.doFilter(request, response);
    }
}
