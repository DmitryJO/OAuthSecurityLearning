package ru.dmsmirnov.config.converter;

import org.springframework.util.StringUtils;

public class IssuerConverter {

    public static String toRealmName(String issuer) {
        if (!StringUtils.hasText(issuer)) {
            return null;
        }

        return issuer.substring(issuer.lastIndexOf('/') + 1);
    }

}
