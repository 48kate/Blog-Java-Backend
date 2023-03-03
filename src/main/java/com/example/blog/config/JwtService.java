package com.example.blog.config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService  {

    private static final String SECRET_KEY = "5368566D597133743677397A244226452948404D635166546A576E5A72347537";

    // generate the token without extraclaims
    public String generateToken (UserDetails userDetails) {

        return  generateToken(new HashMap<>(), userDetails);
    }

    // generate the token out of extraclaims and userdetails
    public String generateToken (
            Map<String, Object> extraClaims, //contain extra claims we want to add
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .claim("pswd", userDetails.getPassword())
                .setIssuedAt(new Date (System.currentTimeMillis())) // when token was created
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24 * 1000)) // how long the token will be valid
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact(); // generate and return the token

    }

    public String extractUsername1(String token) {
        String[] parts = token.split("\\.");
        JSONObject payload = new JSONObject(decode(parts[1]));
        return payload.getString("sub");
    }

    public long extractExpiration1(String token) {
        String[] parts = token.split("\\.");
        JSONObject payload = new JSONObject(decode(parts[1]));
        long exp = payload.getLong("exp");
        return exp;
    }
    // check if username from token equals username from userDetails and token not expired
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername1(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration1(token) < LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static String decode(String encodeString) {
        return new String(Base64.getUrlDecoder().decode(encodeString));
    }


}
