package com.example.livechat.configuration.security;

import com.example.livechat.dao.MyUserDetails;
import com.example.livechat.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

            // 인증이 필요한 경로 목록
            List<String> protectedPaths = Arrays.asList("/menu", "/privatechat");

            // 현재 요청 경로
            String path = request.getRequestURI();

            // 현재 요청 경로가 인증이 필요한 경로인지 확인
            boolean isProtectedPath = protectedPaths.stream().anyMatch(path::startsWith);

            if (isProtectedPath) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("jwtToken".equals(cookie.getName())) {
                            String token = cookie.getValue();

                            if (jwtUtil.isExpired(token)) {
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                                return;
                            }
/*
                            // JWT 토큰 검증 및 인증 처리 로직
                            String authorization = request.getHeader("Authorization");

                            if (authorization == null || !authorization.startsWith("Bearer ")) {
                                // 'Authorization' 헤더가 없거나 Bearer 타입이 아닌 경우 필터 체인을 계속 진행
                                filterChain.doFilter(request, response);
                                return;
                            }

 */
                            // 'Bearer '을 제거하고 순수 토큰만 획득
                            // String token = authorization.substring(7); // "Bearer " 문자열 이후부터가 토큰임

                            // 토큰에서 사용자 정보 추출
                            String username = jwtUtil.getUsername(token);
                            String role = jwtUtil.getRole(token);

                            // 사용자 인증 정보 생성 (여기서는 비밀번호가 필요없으므로 null 혹은 "temp"를 사용)
                            UserEntity userEntity = new UserEntity();
                            userEntity.setUsername(username);
                            userEntity.setRole(role);

                            MyUserDetails myUserDetails = new MyUserDetails(userEntity);
                            Authentication authentication = new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());

                            // SecurityContext에 인증 정보 설정
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            System.out.println("JWTFilter : 토큰 검증 성공");
                        }
                    }
                }
            }

        // SecurityFilterChain의 다음 필터로 요청을 전달
        filterChain.doFilter(request, response);
        }
}
