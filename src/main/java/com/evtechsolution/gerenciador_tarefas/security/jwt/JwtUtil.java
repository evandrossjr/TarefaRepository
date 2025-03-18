package com.evtechsolution.gerenciador_tarefas.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtUtil {
    private static final String SECRET = "seuSegredoSuperSecreto";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public static String generateToken(String username) {
        try {
            return JWT.create()
                    .withSubject(username)
                    .sign(ALGORITHM);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public static String validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado", exception);
        }
    }
}