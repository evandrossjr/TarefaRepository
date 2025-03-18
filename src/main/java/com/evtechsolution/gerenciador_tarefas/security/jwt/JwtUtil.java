package com.evtechsolution.gerenciador_tarefas.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUtil {

    private static final String SECRET = "seuSegredoSuperSecreto";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public static String validateToken(String token) {
        try {
            DecodedJWT jwt = JWT.require(ALGORITHM).build().verify(token);
            return jwt.getSubject(); // Retorna o nome de usuário (subject) do token
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token inválido ou expirado", exception);
        }
    }
}