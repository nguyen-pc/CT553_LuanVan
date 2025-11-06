package com.vn.beta_testing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionInterceptorConfiguration implements WebMvcConfigurer {
    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/", "/api/v1/auth/**", "/storage/**",
                "/api/v1/company/**", "/api/v1/project/**", "/api/v1/campaign/**",
                "/api/v1/campaigns/**", "/api/v1/projects/**", "/api/v1/campaign-type/**", "/api/v1/usecase/**",
                "/api/v1/testcase/**",
                "/api/v1/testscenario/**",
                "/api/v1/recruit-profile/**",
                "/api/v1/user/**",
                 "/api/v1/users/**",
                "/api/v1/test-execution/**",
                "/api/v1/bugs/**",
                "/ws/**",
                "/api/v1/files/**",
                "/api/v1/attachment/**",
                "/api/v1/email/**",
                "/api/v1/email-testers/**",
                "/api/v1/chatbot/**",
                "/api/v1/reward/**",
                "/api/v1/recommend/**",
                "/api/v1/admin/**",
                "/api/v1/project-users/**",

                // "/api/v1/companies/**", "/api/v1/jobs/**", "/api/v1/skills", "/api/v1/files",
                // "/api/v1/resumes/**", "/api/v1/subscribers/**",
                // "/api/v1/positions/**", "/api/v1/questions/**", "/api/v1/skills/**",
                // "/api/v1/jobs-company/**", "api/v1/jobs/follow/**",
                // "api/v1/companies/follow/**",
                // "/api/v1/jobs/recommend"
        };
        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(whiteList);
    }
}
