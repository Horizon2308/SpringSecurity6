package hdh.spring.springsecurity.components;

import hdh.spring.springsecurity.exceptions.InvalidParamException;
import hdh.spring.springsecurity.models.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secret-key}")
    private String secretKey;

    // generate token when user try to sign in or register
    public String generateToken(User user) throws InvalidParamException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .setSubject(user.getUsername())
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new InvalidParamException("Invalid parameters");
        }
    }

    // decode my secret key then parse it to Key
    public Key getSignInKey() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    //exact all claims
    public Claims exactAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T exactClaim(String token, Function<Claims, T> claimResolve) {
        Claims claims = exactAllClaims(token);
        return claimResolve.apply(claims);
    }

    public String exactUsername(String token) {
        return this.exactClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        final Date expirationDate = exactClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = exactUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
