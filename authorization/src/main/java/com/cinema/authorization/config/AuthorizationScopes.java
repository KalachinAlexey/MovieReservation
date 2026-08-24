package com.cinema.authorization.config;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public final class AuthorizationScopes {
    public static final String FILMS_READ = "films:read";
    public static final String FILMS_WRITE = "films:write";
    public static final String EVENTS_READ = "events:read";
    public static final String EVENTS_WRITE = "events:write";
    public static final String HALLS_READ = "halls:read";
    public static final String HALLS_WRITE = "halls:write";
    public static final String RESERVATIONS_READ = "reservations:read";
    public static final String RESERVATIONS_WRITE = "reservations:write";
    public static final String ADMIN = "admin";

    private static final Set<String> USER_SCOPES = Set.of(
            FILMS_READ,
            EVENTS_READ,
            HALLS_READ,
            RESERVATIONS_READ,
            RESERVATIONS_WRITE
    );

    private static final Set<String> ADMIN_SCOPES = Set.of(
            FILMS_READ,
            FILMS_WRITE,
            EVENTS_READ,
            EVENTS_WRITE,
            HALLS_READ,
            HALLS_WRITE,
            RESERVATIONS_READ,
            RESERVATIONS_WRITE,
            ADMIN
    );

    private AuthorizationScopes() {
    }

    public static Set<String> allowedFor(
            Collection<? extends GrantedAuthority> authorities
    ) {
        boolean admin = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);

        return new LinkedHashSet<>(admin ? ADMIN_SCOPES : USER_SCOPES);
    }
}
