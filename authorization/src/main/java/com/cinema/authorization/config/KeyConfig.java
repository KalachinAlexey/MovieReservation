package com.cinema.authorization.config;

import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.*;
import java.security.spec.*;
import java.util.Base64;

@Configuration
public class KeyConfig {

    @Bean
    JWKSource<SecurityContext> jwkSource(
            @Value("${app.security.jwt.public-key}")
            Resource publicKeyResource,

            @Value("${app.security.jwt.private-key}")
            Resource privateKeyResource,

            @Value("${app.security.jwt.key-id}")
            String keyId
    ) throws Exception {
        RSAPublicKey publicKey =
                readPublicKey(publicKeyResource);

        RSAPrivateKey privateKey =
                readPrivateKey(privateKeyResource);

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(keyId)
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);

        return (selector, context) ->
                selector.select(jwkSet);
    }

    @Bean
    JwtDecoder jwtDecoder(
            JWKSource<SecurityContext> jwkSource
    ) {
        return OAuth2AuthorizationServerConfiguration
                .jwtDecoder(jwkSource);
    }

    private RSAPublicKey readPublicKey(
            Resource resource
    ) throws Exception {
        byte[] bytes = decodePem(
                resource,
                "PUBLIC KEY"
        );

        return (RSAPublicKey) KeyFactory
                .getInstance("RSA")
                .generatePublic(
                        new X509EncodedKeySpec(bytes)
                );
    }

    private RSAPrivateKey readPrivateKey(
            Resource resource
    ) throws Exception {
        byte[] bytes = decodePem(
                resource,
                "PRIVATE KEY"
        );

        return (RSAPrivateKey) KeyFactory
                .getInstance("RSA")
                .generatePrivate(
                        new PKCS8EncodedKeySpec(bytes)
                );
    }

    private byte[] decodePem(
            Resource resource,
            String type
    ) throws Exception {
        String pem = resource.getContentAsString(
                StandardCharsets.UTF_8
        );

        String base64 = pem
                .replace(
                        "-----BEGIN " + type + "-----",
                        ""
                )
                .replace(
                        "-----END " + type + "-----",
                        ""
                )
                .replaceAll("\\s", "");

        return Base64.getDecoder().decode(base64);
    }
}