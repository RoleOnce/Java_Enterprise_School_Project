package org.roleonce.enterprise_project.config.security;

import org.roleonce.enterprise_project.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                ) // TODO - Ska det alltid vara en sessionId aktiverad??
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**", "/register", "/login", "/logout").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated())

                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginPage("/login")
                )

                .logout(logoutConfigurer -> logoutConfigurer
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("remember-me", "JSESSIONID")
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                )

                .rememberMe(rememberMeConfigurer -> rememberMeConfigurer
                        .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                        .key("someSecureKey")
                        .userDetailsService(customUserDetailsService)
                        .rememberMeParameter("remember-me")
                );

        return http.build();

    }


}