package com.accenture.academico.bankingsystem.security;

import com.accenture.academico.bankingsystem.domain.user.User;
import com.accenture.academico.bankingsystem.dto.ResponseTokenDTO;
import com.accenture.academico.bankingsystem.exception.InternalErrorException;
import com.accenture.academico.bankingsystem.exception.InternalLogicException;
import com.accenture.academico.bankingsystem.exception.NotAuthorizeException;
import com.accenture.academico.bankingsystem.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    private UserRepository userRepository;

    public ResponseTokenDTO generateToken(User user){
        try {
            Algorithm algorithm =  Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationToken())
                    .sign(algorithm);

            return new ResponseTokenDTO(token,genTokenRefresh(user));

        }catch (JWTCreationException e){
            throw new InternalErrorException("Erro generating token: " + e.getMessage());
        }
    }

    private Instant genExpirationToken(){
        return LocalDateTime.now().plusHours(12).toInstant(ZoneOffset.of("-03:00"));
    }

    public String validateToken(String token){
        try{
            Algorithm algorithm =  Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e){
            return "";
        }
    }

    public boolean isValidToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String genTokenRefresh(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withClaim("user", user.getUsername())
                    .withClaim("codeAccess", CodeAccess.genCode(user.getId()).toString())
                    .withExpiresAt(genExpirationTokenForRefresh())
                    .withClaim("isRefreshToken", true)
                    .sign(algorithm);

        } catch (Exception e) {
            throw new InternalErrorException("Error generating refresh token: " + e.getMessage());
        }
    }

    public ResponseTokenDTO genNewToken(String refreshToken){
        isValidRefreshToken(refreshToken);

        Algorithm algorithm = Algorithm.HMAC256(secret);
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .withIssuer("auth-api")
                .withClaim("isRefreshToken", true)
                .build()
                .verify(refreshToken);

        String userName =  decodedJWT.getClaim("user").asString();
        UUID id =  CodeAccess.reverseCode(UUID.fromString(decodedJWT.getClaim("codeAccess").asString()));
        User user = userRepository.findById(id).orElseThrow();

        if(!user.getEmail().equals(userName)){
            throw new NotAuthorizeException("access denied");
        }
        return generateToken(user);
    }

    private Instant genExpirationTokenForRefresh(){
        return LocalDateTime.now().plusDays(15).toInstant(ZoneOffset.of("-03:00"));
    }

    public void isValidRefreshToken(String refreshToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .withClaim("isRefreshToken", true)
                    .build()
                    .verify(refreshToken);

        } catch (JWTVerificationException e) {
            throw new InternalLogicException("Token Refresh invalid");
        }
    }
}
