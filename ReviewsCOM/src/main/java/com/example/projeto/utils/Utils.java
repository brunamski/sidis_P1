package com.example.projeto.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class Utils {

    @Autowired
    private JwtDecoder jwtDecoder;

    public Long getUserIdByToken(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        String newToken = token.replace("Bearer ", "");
        Jwt decodedToken = this.jwtDecoder.decode(newToken);
        String subject = (String) decodedToken.getClaims().get("sub");
        Long userId = Long.valueOf(subject.split(",")[0]);

        return userId;
    }
}
