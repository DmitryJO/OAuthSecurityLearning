package ru.dm.smirnov.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import ru.dm.smirnov.config.token.JwtAuthenticationConverter;

@Configuration
@RequiredArgsConstructor
public class ProjectConfig {

    private final JwtAuthenticationConverter converter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(c -> c.jwtAuthenticationConverter(converter)));

        http.authorizeHttpRequests(
                c -> c.requestMatchers("/hello").permitAll()
                        .requestMatchers("/manager").hasRole("MANAGER")
                        .requestMatchers("/superuser").hasRole("SUPERUSER")
                        .anyRequest().authenticated()
        );

        return http.build();
    }

}
