package com.afr.fms.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${cors.allowed.origins}")
    private String allowedOrigins; // Comma-separated list from properties

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                List<String> origins = Arrays.asList(allowedOrigins.split(","));

                // 🔹 Apply CORS for all APIs except the exceptions
                registry.addMapping("/**")
                        .allowedOriginPatterns(origins.toArray(new String[0]))
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);

                // 🔹 Exception: Allow `currency_latest` APIs from any origin
                registry.addMapping("/api/currency_container/currency_latest")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET")
                        .allowedHeaders("*")
                        .maxAge(3600);

                registry.addMapping("/api/currency_container/currency_latest/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET")
                        .allowedHeaders("*")
                        .maxAge(3600);
            }
        };
    }
}
