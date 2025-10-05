package com.example.schoolmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Cấu hình CSRF
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**") // Chỉ tắt cho API nếu cần
            )
            
            // Cấu hình authorization
            .authorizeHttpRequests(authorize -> authorize
                // Static resources
                .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                // Auth pages
                .requestMatchers("/", "/login", "/error").permitAll()
                // Dashboard - chỉ cho phép user đã authenticated
                .requestMatchers("/dashboard").authenticated()
                // Admin pages
                .requestMatchers("/schools/**").hasRole("SUPER_ADMIN")
                .requestMatchers("/users/**", "/classes/**", "/subjects/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "TEACHER")
                .requestMatchers("/schedules/**", "/records/**", "/announcements/**").hasAnyRole("ADMIN", "TEACHER")
                // Tất cả request khác cần authenticated
                .anyRequest().authenticated()
            )
            
            // Form login
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login") // URL xử lý login
                .defaultSuccessUrl("/dashboard", true) // Redirect sau khi login thành công
                .failureUrl("/login?error=true") // Redirect khi login thất bại
                .usernameParameter("username") // Tên parameter cho username
                .passwordParameter("password") // Tên parameter cho password
                .permitAll()
            )
            
            // Logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            
            // Exception handling
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/error")
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}