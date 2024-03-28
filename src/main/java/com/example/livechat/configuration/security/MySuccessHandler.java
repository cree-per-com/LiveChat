package com.example.livechat.configuration.security;

import com.example.livechat.dao.MyUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@Component
public class MySuccessHandler implements AuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;

    public MySuccessHandler(JWTUtil jwtUtil) {
        this.jwtUtil=jwtUtil;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
    MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
    String username = myUserDetails.getUsername();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    GrantedAuthority auth = authorities.iterator().next();

    String role = auth.getAuthority();
    String token = jwtUtil.createJwt(username, role, 60 * 60 * 10L); // 토큰 생성
/*
        // 사용자의 추가 정보를 세션에 저장
        HttpSession session = request.getSession();
        User user = (User) authentication.getPrincipal(); // User는 Spring Security의 UserDetails 구현체
        session.setAttribute("userInfo", user.getUsername()); // 예시로, 사용자 이름을 세션에 저장
 */

    // JWT 토큰을 JSON 응답으로 전송
    response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String jsonResponse = String.format("{\"authToken\": \"%s\"}", token);
        out.print(jsonResponse);
        out.flush();

        // 성공 응답 처리 등 기타 필요한 로직
      //  response.sendRedirect("/privatechat");
}

}
