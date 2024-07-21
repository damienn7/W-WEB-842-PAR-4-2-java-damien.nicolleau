package com.cwa.crudspringboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;


import com.cwa.crudspringboot.filter.JwtFilter;
import com.cwa.crudspringboot.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // .authorizeHttpRequests(auth ->
        // auth.requestMatchers("/api/auth/*").permitAll()
        //         .anyRequest().authenticated())
        
        // return http
        //         .csrf(AbstractHttpConfigurer::disable)
        //         .authorizeHttpRequests(auth -> auth
        //             .requestMatchers("/", "/login", "/signup","/css/**", "/js/**", "/images/**", "/api/auth/*").permitAll()
        //             .requestMatchers("/user/**","/user").hasAuthority("ROLE_USER")
        //             .anyRequest().authenticated()
        //         )
        //         // .addFilterBefore(new JwtFilter(customUserDetailsService, jwtUtils), UsernamePasswordAuthenticationFilter.class)
        //         .build();

        // return http
        //     .authorizeRequests()
        //         .requestMatchers("/signup", "/login").permitAll()
        //         .anyRequest().authenticated()
        //         .and()
        //     .formLogin()
        //         .loginPage("/login")
        //         .defaultSuccessUrl("/user", true)
        //         .permitAll()
        //         .and()
        //     .logout()
        //         .permitAll();

        CookieClearingLogoutHandler cookies = new CookieClearingLogoutHandler("JSESSIONID");

        http
        .authorizeHttpRequests((requests) -> requests
            .requestMatchers("/", "/*", "/login", "/signup","/css/**", "/js/**", "/images/**", "/api/auth/*").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin((form) -> form
            .loginPage("/login")
            .defaultSuccessUrl("/redirect")
            .permitAll()
        )
        .logout((logout) -> logout
            .logoutSuccessUrl("/")
            .deleteCookies("token","JSESSIONID")
            .clearAuthentication(true)
            .invalidateHttpSession(true)
            .addLogoutHandler(cookies)
            .permitAll()
            );

        return http.build();
    }
}
