package com.packt.football.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        InMemoryUserDetailsManager userDetailsManager() {
                UserDetails userAdmin = User.withDefaultPasswordEncoder()
                                .username("packt")
                                .password("packt")
                                .roles("ADMIN")
                                .build();
                UserDetails simpleUser = User.withDefaultPasswordEncoder()
                                .username("user1")
                                .password("user1")
                                .roles("USER")
                                .build();
                return new InMemoryUserDetailsManager(userAdmin, simpleUser);
        }

        @Bean
        WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring()
                                .requestMatchers("/security/public/**");
        }

        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http
                                .authorizeHttpRequests(authorizeRequests -> {
                                        authorizeRequests
                                                        .requestMatchers("/security/private/**").hasRole("ADMIN")
                                                        .anyRequest().permitAll();
                                })
                                .httpBasic(withDefaults())
                                .build();
        }
}
