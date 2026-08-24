package com.cinema.authorization.config;

import org.springframework.context.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.*;

import java.util.List;
import java.util.Set;

@Configuration
public class TokenConfig {

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext>
    jwtTokenCustomizer() {
        return context -> {
            if (!OAuth2TokenType.ACCESS_TOKEN.equals(
                    context.getTokenType()
            )) {
                return;
            }

            List<String> roles = context
                    .getPrincipal()
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            Set<String> allowedScopes = AuthorizationScopes.allowedFor(
                    context.getPrincipal().getAuthorities()
            );

            List<String> grantedScopes = context.getAuthorizedScopes()
                    .stream()
                    .filter(allowedScopes::contains)
                    .sorted()
                    .toList();

            context.getClaims().audience(
                    List.of("reservation-api")
            );

            context.getClaims().claim(
                    "roles",
                    roles
            );

            context.getClaims().claim(
                    OAuth2ParameterNames.SCOPE,
                    grantedScopes
            );
        };
    }
}
