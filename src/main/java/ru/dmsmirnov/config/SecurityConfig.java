package ru.dmsmirnov.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;
import ru.dmsmirnov.config.converter.token.JwtAuthenticationConverter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationConverter converter;

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

    @Bean
    public JwtIssuerAuthenticationManagerResolver authenticationManagerResolver() {
        return new JwtIssuerAuthenticationManagerResolver(new CustomAuthenticationResolver(converter));
    }

}
