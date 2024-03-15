package com.example.livechat.configuration.security;

import com.example.livechat.dao.MyUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    public LoginFilter(AuthenticationManager authenticationManager,JWTUtil jwtUtil) {
        this.authenticationManager=authenticationManager;
        this.jwtUtil=jwtUtil;
        super.setAuthenticationManager(authenticationManager);
        super.setFilterProcessesUrl("/loginProc"); // 로그인 처리 URL 설정
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
                                         Authentication au) throws IOException, ServletException {

        MyUserDetails myUserDetails = (MyUserDetails) au.getPrincipal();
        String username = myUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = au.getAuthorities();
        GrantedAuthority auth = authorities.iterator().next();
        String role = auth.getAuthority();

        //JWT 토큰 생성
        String token = jwtUtil.createJwt(username,role,60*60*10L);

        //쿠키 생성 및 응답에 추가
        Cookie cookie = new Cookie("JWT_TOKEN",token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        //세션에 인증 토큰과 사용자 정보 저장
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        session.setAttribute("jwtToken",token);
        session.setAttribute("USER_DETAILS",myUserDetails);
        //현재 SecurityContext에 접근해서 인증 객체를 au로 설정
        SecurityContextHolder.getContext().setAuthentication(au);

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
