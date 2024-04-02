package com.example.livechat.configuration.websocket;

import com.example.livechat.configuration.security.JWTUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;


@Slf4j
public class AuthHandshakeInterceptor implements HandshakeInterceptor {
    private final JWTUtil jwtUtil;

    public AuthHandshakeInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler handler,
                                   Map<String,Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletServerHttpRequest) {

            Cookie[] cookies = servletServerHttpRequest.getServletRequest().getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwtToken".equals(cookie.getName())) {
                        String token = cookie.getValue();

                        if (!jwtUtil.isExpired(token)) return true;
                    }
                }
            }
        }else log.error("Request type이 ServletServerHttpRequest가 아닙니다.",request.getClass().getName());
            //쿠키가 없으면 핸드셰이크를 중단함.
            return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler handler,
                               @Nullable Exception exception) {
        //핸드셰이크 완료 후 로직
        if (exception != null) {
            // 핸드셰이크 과정에서 발생한 예외를 로깅
            log.error("WebSocket 핸드셰이크 실패: ", exception);
        } else {
            // 핸드셰이크 성공 로깅
            log.info("WebSocket 핸드셰이크 성공");
        }

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpSession session = servletRequest.getServletRequest().getSession();
            Object username = session.getAttribute("username");
            log.info("WebSocket 세션에 설정된 username: {}", username);
        }
    }
}
