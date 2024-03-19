package com.example.livechat.configuration.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final MySuccessHandler mySuccessHandler;

    public LoginFilter(AuthenticationManager authenticationManager, MySuccessHandler mySuccessHandler) {
        super.setAuthenticationManager(authenticationManager);
        this.mySuccessHandler=mySuccessHandler;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
       //필터가 요청에서 username과 비번 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
      //authenticationManager에게 토큰 전달
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {


        mySuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) {
        logger.debug("Authentication failed: " + e.getMessage());
        response.setStatus(401);
    }
}
