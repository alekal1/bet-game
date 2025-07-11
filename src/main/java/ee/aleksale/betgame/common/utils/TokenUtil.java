package ee.aleksale.betgame.common.utils;

import ee.aleksale.betgame.auth.model.domain.PlayerEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

@UtilityClass
public class TokenUtil {

    private static final String SECRET_KEY = "4261656C64756E674261656C64756E674261656C64756E674261656C64756E67";
    private static final String BEARER_TOKEN_PREFIX = "Bearer";

    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null && token.contains(BEARER_TOKEN_PREFIX)) {
            return Arrays.stream(token.split(" ")).skip(1).findFirst().orElse(null);
        }
        return null;
    }

    public static String getUsername(String token) {
        if (token == null) {
            return null;
        }
        return extractClaim(token, Claims::getSubject);
    }

    public static Long getId(String token) {
        return Long.parseLong(extractClaim(token, claims -> claims.get("id")).toString());
    }

    public static String generateToken(PlayerEntity playerEntity) {
        return Jwts.builder()
                .subject(playerEntity.getUsername())
                .claim("id", playerEntity.getId())
                .issuedAt(new Date())
                .signWith(getSigningKey())
                .compact();
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}