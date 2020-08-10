package com.multiple_language_menu.filters;

import com.multiple_language_menu.models.auth.CustomUserDetail;
import com.multiple_language_menu.models.request.ReqLogin;
import com.multiple_language_menu.services.authorize.AttributeTokenService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.*;

@Component
@Slf4j
public class JwtTokenProvider {
    static final long EXPIRATIONTIME = 86_400_000; // 1 day
    public static final String SECRET = "SecretKey";
    public static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";
    static final String LOGIN_URI = "/login";
    static final String LOGOUT_URI = "/logout";
    static final String SHOP_URI = "/shop";
    static final String CATEGORY_URI = "/category";
    static final String ITEM_URI = "/item";

    public String generateJwt(CustomUserDetail userDetail) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATIONTIME);

        //Tạo JWT
        return Jwts.builder()
                .setSubject((userDetail.getUser().getUsername()))
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .claim("password", userDetail.getPassword())
                .claim("username",userDetail.getUsername())
                .claim("roles", userDetail.getRoles())
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    // Lấy thông tin user từ jwt
    public String getUserNameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
