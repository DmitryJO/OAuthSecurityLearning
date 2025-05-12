package ru.dm.smirnov.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ProjectConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtIssuerAuthenticationManagerResolver resolver) throws Exception {
        http.oauth2ResourceServer(oauth2 -> oauth2
                .authenticationManagerResolver(resolver));

        http.authorizeHttpRequests(
                c -> c.requestMatchers("/hello").permitAll()
                        .requestMatchers("/manager").hasAnyAuthority("manager", "admin")
                        .requestMatchers("/superuser").hasAuthority("admin")
                        .anyRequest().authenticated()
        );

        return http.build();
    }

}
