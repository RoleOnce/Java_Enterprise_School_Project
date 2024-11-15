package org.roleonce.projektarbete_web_services.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register", "/login", "logout").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .anyRequest().authenticated())

                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginPage("/login")
                )

                .logout(logoutConfigurer -> logoutConfigurer
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)      // TODO - Should Clear Authentication?
                        .deleteCookies("remember-me", "JSESSIONID")
                        .logoutUrl("/custom-logout")           // TODO - Endpoint for logging out?
                );

        return http.build();

    }
}
