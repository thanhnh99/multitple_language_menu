package com.multiple_language_menu.services;

import com.multiple_language_menu.models.entities.Roles;
import com.multiple_language_menu.models.entities.Users;
import com.multiple_language_menu.repositories.IUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security
        .authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static java.util.Collections.emptyList;
@Service
public class TokenAuthenticationService {
    @Autowired
    IUserRepository userRepository;
    static final long EXPIRATIONTIME = 864_000_000; // 10 days
    static final String SECRET = "ThisIsASecret";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    public static void addAuthentication(HttpServletResponse res, String username) {
        String JWT = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + JWT);
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        String t = token.replace(TOKEN_PREFIX, "");
        if (token != null) {
            String username = AttributeTokenService.getUsernameFromToken(token);
            return username != null ?
                    new UsernamePasswordAuthenticationToken(username, null, emptyList()) :
                    null;
        }
        return null;
    }
}

