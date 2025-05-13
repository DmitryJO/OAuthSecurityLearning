package ru.dmsmirnov.config.converter.token;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Setter
public class RealmAccessGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS_KEY = "realm_access";
    private static final String ROLES_KEY = "roles";

    protected Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter;

    public RealmAccessGrantedAuthoritiesConverter() {
        this.jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        ((JwtGrantedAuthoritiesConverter) jwtGrantedAuthoritiesConverter).setAuthorityPrefix("");
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = Collections.emptyList();

        // Получение прав из scope
        try {
            grantedAuthorities = jwtGrantedAuthoritiesConverter.convert(jwt);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        // Получение групп из realm_access.roles
        try {
            return getRoleAuthorities(jwt, grantedAuthorities);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return grantedAuthorities;
    }

    private Collection<GrantedAuthority> getRoleAuthorities(Jwt jwt, Collection<GrantedAuthority> grantedAuthorities) {
        Collection<GrantedAuthority> authorities;
        if (CollectionUtils.isEmpty(grantedAuthorities)) {
            authorities = new ArrayList<>();
        } else {
            authorities = new ArrayList<>(grantedAuthorities);
        }

        Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS_KEY);
        if (CollectionUtils.isEmpty(realmAccess)) {
            return authorities;
        }
        realmAccess.get(ROLES_KEY);
        List<String> roles = (List<String>) realmAccess.get(ROLES_KEY);

        List<SimpleGrantedAuthority> roleAuthorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        authorities.addAll(roleAuthorities);

        return authorities;
    }

}
