package com.multiple_language_menu.services;

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


    public static String getRolesFromToken(String token)
    {
        Object o = getAllClaimsFromToken(token).get("roles");
        if (instanceOf()) {
            
        }
        List<String> data = (ArrayList) getAllClaimsFromToken(token).get("roles");

        return "Done";
    }
//
//    public ArrayList<String> getRolesFromToken(HttpServletRequest request)
//    {
//        try {
//            String token = request.getHeader(HEADER_STRING);
//            String t = token.replace(TOKEN_PREFIX, "");
//            if(token != null)
//            {
//                String username = Jwts.parser()
//                        .setSigningKey(SECRET)
//                        .parseClaimsJws(token.replace(TOKEN_PREFIX + " ", ""))
//                        .getBody()
//                        .getSubject();
//
//                Users user = userRepository.findByUsername(username);
//                ArrayList<String> roles = new ArrayList<String>();
//                for(Roles role : user.getRoles())
//                {
//                    roles.add(role.getName());
//                }
//                return roles;
//            }
//        }catch (Exception e)
//        {
//            System.out.println("Err TokenAuthenticationService.getRolesFromToken: " + e.getMessage());
//            return null;
//        }
//        return null;
//    }
}
