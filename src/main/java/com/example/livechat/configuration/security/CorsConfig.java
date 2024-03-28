package com.example.livechat.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 쿠키/인증 정보를 포함한 요청 허용
        config.addAllowedOrigin("*"); // 모든 Origin 허용
        config.addAllowedHeader("*"); // 모든 Header 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메소드 허용
        config.addExposedHeader("Authorization"); // 클라이언트가 접근할 수 있도록 'Authorization' 헤더 노출

        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 위의 설정 적용
        return source;
    }
}
