package com.packt.footballresource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.GET, "/football/teams/**")
                        .hasAuthority("SCOPE_football:read")
                        .requestMatchers(HttpMethod.POST, "/football/teams/**").hasAuthority("SCOPE_football:admin")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

//     @Bean
//     public SecurityFilterChain filterChainRoles(HttpSecurity http) throws Exception {
//         return http
//                 .authorizeHttpRequests(
//                         authorize -> authorize.requestMatchers(HttpMethod.POST, "football/teams/**").hasRole("ADMIN")
//                                 .anyRequest().authenticated())
//                 .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
//                 .build();
//     }
}
