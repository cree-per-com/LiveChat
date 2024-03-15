package com.example.livechat.configuration.security;

import com.example.livechat.configuration.security.JWTUtil;
import com.example.livechat.dao.MyUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    public LoginFilter(AuthenticationManager authenticationManager,JWTUtil jwtUtil) {
        this.authenticationManager=authenticationManager;
        this.jwtUtil=jwtUtil;}
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
                                         Authentication au) throws IOException {
        MyUserDetails myUserDetails = (MyUserDetails) au.getPrincipal();
        String username = myUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = au.getAuthorities();
        Iterator<? extends  GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        String token = jwtUtil.createJwt(username,role,60*60*10L);
        response.addHeader("Authorization","Bearer"+token);

        //defaultSuccessUrl 직접 구현
        response.sendRedirect("/menu");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) {
        response.setStatus(401);
    }
}
