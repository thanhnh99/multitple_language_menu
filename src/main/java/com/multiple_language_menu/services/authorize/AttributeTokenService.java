package com.multiple_language_menu.services.authorize;

import com.multiple_language_menu.filters.TokenJwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public  class AttributeTokenService {
    private static final String SECRET = TokenJwtUtil.SECRET;
    private static final String TOKEN_PREFIX = TokenJwtUtil.TOKEN_PREFIX;

    //retrieve username from jwt token
    public static String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }


    //retrieve expiration date from jwt token
    public static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public static  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    //for retrieveing any information from token we will need the secret key
    public static Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
    }


    public static List<String> getRolesFromToken(String token)
    {
        Object o = getAllClaimsFromToken(token).get("roles");
        if ( o instanceof List) {
            List<String> data = (ArrayList) getAllClaimsFromToken(token).get("roles");
            return data;
        }
        return null;
    }

    public static boolean checkAccess(String token, String roleCheck)
    {
        List<String> roleFromTokens = getRolesFromToken(token);
        if(roleFromTokens != null)
        {
            for(String role : roleFromTokens)
            {
                if(roleCheck.equals(role))
                {
                    return true;
                }
            }
        }
        return false;
    }

}
