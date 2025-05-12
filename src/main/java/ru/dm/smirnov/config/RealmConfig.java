package ru.dm.smirnov.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.util.ObjectUtils;
import ru.dm.smirnov.config.converter.IssuerConverter;
import ru.dm.smirnov.config.converter.token.JwtAuthenticationConverter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RealmConfig {

    @Value("${resource.server.uri}")
    private String resourceServerUrl;

    private final JwtAuthenticationConverter converter;

    private static final String KEYCLOAK_JWKS_URI = "/protocol/openid-connect/certs";
    private static final Map<String, JwtAuthenticationProvider> providers = new HashMap<>();

    @Bean
    public JwtIssuerAuthenticationManagerResolver authenticationManagerResolver() {
        return new JwtIssuerAuthenticationManagerResolver(new CustomResolver());
    }

    public class CustomResolver implements AuthenticationManagerResolver<String> {

        @Override
        public AuthenticationManager resolve(String issuer) {
            if (!issuer.contains(resourceServerUrl)) {
                return null;
            }
            String realmName = IssuerConverter.toRealmName(issuer);
            JwtAuthenticationProvider oldProvider = providers.get(realmName);
            if (!ObjectUtils.isEmpty(oldProvider)) {
                return new ProviderManager(oldProvider);
            }

            JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(issuer + KEYCLOAK_JWKS_URI).build();
            JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoder);
            provider.setJwtAuthenticationConverter(converter);

            providers.put(realmName, provider);
            return new ProviderManager(provider);
        }

    }

}
