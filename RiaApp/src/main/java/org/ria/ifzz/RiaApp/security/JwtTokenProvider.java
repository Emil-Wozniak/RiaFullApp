package org.ria.ifzz.RiaApp.security;

import io.jsonwebtoken.*;
import org.ria.ifzz.RiaApp.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.ria.ifzz.RiaApp.security.SecurityConstants.EXPIRATION_TIME;
import static org.ria.ifzz.RiaApp.security.SecurityConstants.SECRET;

/**
 * JwtTokenProvider generate token with all needed contents on it
 */

@Component
public class JwtTokenProvider {

    public String generateToken(Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());

        Date expiredDate = new Date(now.getTime() + EXPIRATION_TIME);
        String userId = Long.toString(user.getId());

        /**
         * claims represent the content of the token
         * it is possible to put roles in the token
         */
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", (Long.toString(user.getId())));
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullName());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * validate the token
     *
     * @param token
     * @return true if token is invalid or false if it is correct
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT Signature;");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token;");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token;");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token;");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty;");
        }
        return false;
    }

    /**
     * get user id from token
     *
     * @param token
     * @return user id
     */
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String) claims.get("id");
        System.out.println("Claims get: " + id);
        return Long.parseLong(id);
    }
}
