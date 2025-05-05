package ru.dm.smirnov.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/home", "/", "/login/**", "/logout").permitAll()
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(o -> o
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                )
                .logout(t -> t
                        .logoutSuccessUrl("/home")
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new CustomEntryPoint())
                );

        return http.build();
    }

}