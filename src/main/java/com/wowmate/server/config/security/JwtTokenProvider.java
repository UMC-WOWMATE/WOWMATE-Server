package com.wowmate.server.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * 클라이언트에서 보내주는 token을 통해 email을 얻는 방법
 *
 *     private final JwtTokenProvider jwtTokenProvider;
 *
 *     public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
 *         this.jwtTokenProvider = jwtTokenProvider;
 *     }
 *
 *     을 선언하고
 *     String token = jwtTokenProvider.resolveToken(servletRequest);
 *     String email = jwtTokenProvider.getUserEmail(token)
 **/
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey = "secretKey"; // yml에서 값을 가져올 수 없다면 기본값으로 받아옴
    private Key key;
    private final long tokenValidMillisecond = 1000L * 60 * 60 * 24 * 30;

    @PostConstruct
    protected void init() {
        log.info("[init] JwtTokenProvider 내 secretKey 초기화 시작");
        byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8));
        this.key = Keys.hmacShaKeyFor(keyBytes);    // keyBytes 와 이에 맞는 암호화 함수를 객체 형태로 반환
        log.info("[init] JwtTokenProvider 내 secretKey 초기화 완료");
    }

    public String createToken(String userUid, List<String> roles) {
        log.info("[createToken] 토큰 생성 시작");
        Claims claims = Jwts.claims().setSubject(userUid);
        log.info("Jwts에서 claims 생성 claims : {}", claims);
        claims.put("roles", roles);
        log.info("claims에 roles 추가 claims : {}" , claims);
        Date now = new Date();
        log.info("Date 생성 Date : {} ", now);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(key)
                .compact();

        log.info("[createToken] 토큰 생성 완료 token : {}", token);

        return token;
    }

    // JWT 토큰으로 인증 정보 조회
    public Authentication getAuthentication(String token) {
        log.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(token));
        log.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails UserName : {}",
                userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

    // JWT 토큰에서 회원 구별 정보 추출
    public String getUserEmail(String token) {
        log.info("[getUserEmail] 토큰 기반 회원 구별 정보 추출");
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        String email = parser.parseClaimsJws(token).getBody().getSubject();
        log.info("[getUserEmail] 토큰 기반 회원 구별 정보 추출 완료, Email : {}", email);
        return email;
    }
    /**
     * HTTP Request Header 에 설정된 토큰 값을 가져옴
     *
     * @param request Http Request Header
     * @return String type Token 값
     */
//    public String resolveToken(HttpServletRequest request){
//        log.info("[resolveToken] HTTP 헤더에서 Token 값 추출");
//        return request.getHeader("Authorization"); // Bearer accessToken
//    }

    public String resolveToken(HttpServletRequest request) {
        log.info("[resolveToken] HTTP 헤더에서 Token 값 추출");
        String header = request.getHeader("Authorization");
        String token = header.split(" ")[1].trim();
        return token;
    }

    // JWT 토큰의 유효성 + 만료일 체크
    public boolean validateToken(String token) {
        log.info("[validateToken] 토큰 유효 체크 시작");
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build();
            Jws<Claims> claims = parser.parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }
}
