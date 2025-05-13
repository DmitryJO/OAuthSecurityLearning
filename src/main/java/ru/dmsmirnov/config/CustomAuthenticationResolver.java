package ru.dmsmirnov.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.util.ObjectUtils;
import ru.dmsmirnov.config.converter.IssuerConverter;
import ru.dmsmirnov.config.converter.token.JwtAuthenticationConverter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CustomAuthenticationResolver implements AuthenticationManagerResolver<String> {

    private static final String KEYCLOAK_JWKS_URI = "/protocol/openid-connect/certs";
    private static final Map<String, JwtAuthenticationProvider> providers = new HashMap<>();

    @Value("${resource.server.uri}")
    private String resourceServerUrl;
    private JwtAuthenticationConverter converter;

    @Autowired
    public CustomAuthenticationResolver(JwtAuthenticationConverter converter) {
        this.converter = converter;
    }

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
