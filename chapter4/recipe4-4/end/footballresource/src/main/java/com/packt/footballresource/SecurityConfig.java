package com.packt.footballresource;

import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadJwtGrantedAuthoritiesConverter;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/football/teams/**").hasAnyAuthority("APPROLE_football.read", "APPROLE_football.admin")
                        .requestMatchers(HttpMethod.POST, "/football/teams/**").hasAnyAuthority("APPROLE_football.admin")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> aadJwtGrantedAuthoritiesConverter() {
        return new AadJwtGrantedAuthoritiesConverter();
    }

    @Bean
    public JwtAuthenticationConverter aadJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(aadJwtGrantedAuthoritiesConverter());
        return converter;
    }
}
