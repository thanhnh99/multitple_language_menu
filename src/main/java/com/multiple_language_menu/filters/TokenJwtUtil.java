package com.multiple_language_menu.filters;

import com.multiple_language_menu.models.entities.Users;
import com.multiple_language_menu.models.request.ReqLogin;
import com.multiple_language_menu.repositories.IUserRepository;
import com.multiple_language_menu.services.AttributeTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.AutoPopulatingList;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


import static java.util.Collections.emptyList;
public class TokenJwtUtil {
    static final long EXPIRATIONTIME = 86_400_000; // 1 day
    public static final String SECRET = "SecretKey";
    public static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    public static String generateJwt(ReqLogin reqLogin, List<String> roles) {
        long expirationTime = EXPIRATIONTIME;
        return Jwts.builder()
                .setSubject(reqLogin.getUsername())
                .claim("password", reqLogin.getPassword())
                .claim("username",reqLogin.getUsername())
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public  Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token
            String username = AttributeTokenService.getUsernameFromToken(token);
            return username != null ? new UsernamePasswordAuthenticationToken(username, null,new ArrayList<>()) : null;
        }
        return null;
    }
}
