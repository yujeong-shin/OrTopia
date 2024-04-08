package com.example.ordering_lecture.token;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.token.recommend-expiration-time}")
    private long recommendExpirationTime;

    public JwtFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String accessToken = bearerToken.substring(7);
            try {
                Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(accessToken).getBody();
                // JWT 토큰으로부터 사용자의 정보를 추출하는 예시 코드
                String email = claims.getSubject();
                String role = claims.get("role", String.class);

                // Spring Security의 Authentication 객체를 생성하고 SecurityContext에 설정
                List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role);
                Authentication auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);


            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid token");
                return; // 이 경우에는 다음 필터나 컨트롤러로 요청을 넘기지 않고 종료합니다.
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication failed");
                return; // 이 경우에는 다음 필터나 컨트롤러로 요청을 넘기지 않고 종료합니다.
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization token is missing");
            return; // 이 경우에는 다음 필터나 컨트롤러로 요청을 넘기지 않고 종료합니다.
        }

        filterChain.doFilter(request, response); // 다음 필터로 요청을 넘깁니다.
    }

}
