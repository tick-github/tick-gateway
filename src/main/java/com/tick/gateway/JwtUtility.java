package com.tick.gateway;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Map;

@Component
public class JwtUtility {

    private static final String GOOGLE_JWK_URI = "https://www.googleapis.com/oauth2/v3/certs";

    public static Map<String, Claim> getClaims(String token) {

        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaims();
        } catch (JWTDecodeException e) {
            return Collections.emptyMap();
        }

    }

    public static boolean verify(String token) {

        try {
            // Signature Verification
            DecodedJWT jwt = JWT.decode(token);
            JwkProvider provider = new UrlJwkProvider(GOOGLE_JWK_URI);
            Jwk jwk = provider.get(jwt.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(jwt);

            // Expiration Verification
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
        } catch (JwkException | JWTVerificationException e) {
            return false;
        }

        return true;
    }

}
