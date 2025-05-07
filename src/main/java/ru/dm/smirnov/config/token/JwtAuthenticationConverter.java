package ru.dm.smirnov.config.token;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, CustomAuthentication> {

    protected Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new RealmAccessGrantedAuthoritiesConverter();

    @Override
    public CustomAuthentication convert(Jwt source) {
        return new CustomAuthentication(source, getAuthority(source));
    }

    public Collection<GrantedAuthority> getAuthority(Jwt jwt) {
        return this.jwtGrantedAuthoritiesConverter.convert(jwt);
    }

}
