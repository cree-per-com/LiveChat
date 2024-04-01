package com.example.livechat.configuration.websocket;

import com.example.livechat.configuration.security.JWTFilter;
import jakarta.servlet.http.Cookie;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class AuthHandshakeInterceptor implements HandshakeInterceptor {
    private final JWTFilter jwtFilter;

    public AuthHandshakeInterceptor(JWTFilter jwtFilter) {
        this.jwtFilter=jwtFilter;
    }
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler handler,
                                   Map<String,Object> attributes) throws Exception{
        if(request instanceof ServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            Cookie[] cookies = servletServerHttpRequest.getServletRequest().getCookies();
            String authToken = null;

            if(cookies!=null) {
                for (Cookie cookie : cookies) {
                    if("jwtToken".equals(cookie.getName())) {
                        authToken = cookie.getValue();
                        break;
                    }
                }
            }

            if(authToken!=null && validateToken(authToken)) return true;
            else return false;
        }
        //쿠키가 없는 경우 핸드셰이크를 중단함.
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler handler,
                               Exception exception) {

    }

    //토큰이 유효한지 검사
    private boolean validateToken(String token) {

        return true;
    }
}
