package com.packt.footballui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.azure.spring.cloud.autoconfigure.implementation.aadb2c.security.AadB2cOidcLoginConfigurer;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfiguration {

        private final AadB2cOidcLoginConfigurer configurer;

        public SecurityConfiguration(AadB2cOidcLoginConfigurer configurer) {
                this.configurer = configurer;
        }

        

        @Bean
        public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
                        throws Exception {

                http
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers("/").permitAll()
                                                .anyRequest().authenticated())
                                .apply(configurer);

                return http.build();
        }

}
