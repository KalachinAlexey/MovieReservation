package com.cinema.authorization.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationScopesTests {

    @Test
    void userCanReadCatalogAndManageReservations() {
        assertThat(AuthorizationScopes.allowedFor(
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        )).containsExactlyInAnyOrder(
                "films:read",
                "events:read",
                "halls:read",
                "reservations:read",
                "reservations:write"
        );
    }

    @Test
    void adminHasEveryScope() {
        assertThat(AuthorizationScopes.allowedFor(
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        )).containsExactlyInAnyOrder(
                "films:read",
                "films:write",
                "events:read",
                "events:write",
                "halls:read",
                "halls:write",
                "reservations:read",
                "reservations:write",
                "admin"
        );
    }
}
