package ru.dm.smirnov.config.token;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

public class CustomAuthentication extends JwtAuthenticationToken {

    public CustomAuthentication(Jwt jwt,
                                Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
    }

}
