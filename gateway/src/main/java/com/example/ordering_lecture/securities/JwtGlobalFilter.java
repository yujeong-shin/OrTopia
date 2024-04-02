package com.example.ordering_lecture.securities;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtGlobalFilter implements GlobalFilter {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Autowired
    private RedisTemplate<String, String> redisTemplate; // Changed to String, String

    private final List<String> allowUrl = Arrays.asList("/member/create", "/doLogin");
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        System.out.println("===ex===");
        System.out.println(exchange);
        System.out.println("===re===");
        System.out.println(request);
        String reqUri = request.getURI().getPath();
        boolean isAllowed = allowUrl.stream().anyMatch(uri -> antPathMatcher.match(uri, reqUri));

        if (isAllowed) {
            return chain.filter(exchange);
        }

        String bearerToken = request.getHeaders().getFirst("Authorization");
        System.out.println(bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String accessToken = bearerToken.substring(7);
            try {
                Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody();
                String email = claims.getSubject();
                String role = claims.get("role", String.class);
                request = exchange.getRequest().mutate()
                        .header("myEmail", email)
                        .header("myRole", role)
                        .build();
                exchange = exchange.mutate().request(request).build();
            } catch (ExpiredJwtException e) {
                return validateRefreshTokenAndGenerateNewAccessToken(exchange, e.getClaims().getSubject(), e.getClaims().get("role", String.class));
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
                return onError(exchange, "Invalid token", HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return onError(exchange, "Authentication failed", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return onError(exchange, "Authorization token is missing", HttpStatus.UNAUTHORIZED);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        response.getHeaders().add("Content-Type", "application/json");
        return response.writeWith(Mono.just(buffer));
    }

    private Mono<Void> validateRefreshTokenAndGenerateNewAccessToken(ServerWebExchange exchange, String email, String role) {
        // 'Authorization' 헤더 대신 'X-Refresh-Token' 헤더에서 리프레시 토큰을 추출
        String refreshToken = exchange.getRequest().getHeaders().getFirst("X-Refresh-Token");
        String storedRefreshToken = redisTemplate.opsForValue().get("RT:" + email);

        if (refreshToken != null && refreshToken.equals(storedRefreshToken)) {
            String newAccessToken = generateNewAccessToken(email, role);
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().set("New-Access-Token", newAccessToken);
            return response.setComplete();
        } else {
            return onError(exchange, "Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }
    }


    private String generateNewAccessToken(String email, String role) {
        long expirationTimeLong = 3600000; // 예: 1시간
        String secretKey = "mysecret"; // 실제 비밀키로 교체해야 합니다.

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTimeLong);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // 필요에 따라 추가 클레임을 포함할 수 있습니다.

        // 새 액세스 토큰 생성
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }


}
