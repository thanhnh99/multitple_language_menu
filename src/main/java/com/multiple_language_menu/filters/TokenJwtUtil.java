package com.multiple_language_menu.filters;

import com.multiple_language_menu.models.request.ReqLogin;
import com.multiple_language_menu.services.authorize.AttributeTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.ResolvableType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.*;

public class TokenJwtUtil {
    static final long EXPIRATIONTIME = 86_400_000; // 1 day
    public static final String SECRET = "SecretKey";
    public static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";
    static final String LOGIN_URI = "/login";
    static final String LOGOUT_URI = "/logout";

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
        String securedPath = request.getRequestURI();
        if(securedPath.equals(LOGIN_URI) || securedPath.equals(LOGOUT_URI))
        {
            return new UsernamePasswordAuthenticationToken(LOGIN_URI, null,new ArrayList<>());
        }
        if( token != null){
            // parse the token
            String username = AttributeTokenService.getUsernameFromToken(token);
            if(username != null)
                return username != null ? new UsernamePasswordAuthenticationToken(username, null,new ArrayList<>()) : null;
        }
        return null;
    }


        // Lay ra securedPath duoc Annotate RequestMapping trong Controller
    private String extractSecuredPath(Object callerObj) {
        Class<?> clazz = ResolvableType.forClass(callerObj.getClass()).getRawClass();
        Optional<Annotation> annotation = Arrays.asList(clazz.getAnnotations()).stream().filter((ann) -> {
            return ann instanceof RequestMapping;
        }).findFirst();
        if (annotation.isPresent()) {
            return ((RequestMapping) annotation.get()).value()[0];
        }
        return null;
    }
}
