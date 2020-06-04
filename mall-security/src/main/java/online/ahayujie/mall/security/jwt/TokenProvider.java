package online.ahayujie.mall.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * jwt提供类
 * @author aha
 * @date 2020/3/25
 */
@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    private final String base64AccessSecret;
    private final String base64RefreshSecret;
    private final Long accessTokenValidityInMilliseconds;
    private final Long refreshTokenValidityInMilliseconds;

    private final JwtUserDetailService jwtUserDetailService;

    private static Key accessKey;
    private static Key refreshKey;

    public TokenProvider(@Value("${jwt.base64-access-secret}") String base64AccessSecret,
                         @Value("${jwt.base64-refresh-secret}") String base64RefreshSecret,
                         @Value("${jwt.access-token-validity-in-seconds}") Long accessTokenValidityInSeconds,
                         @Value("${jwt.refresh-token-validity-in-seconds}") Long refreshTokenValidityInSeconds,
                         JwtUserDetailService jwtUserDetailService) {
        this.base64AccessSecret = base64AccessSecret;
        this.base64RefreshSecret = base64RefreshSecret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
        this.jwtUserDetailService = jwtUserDetailService;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] accessKeyBytes = Decoders.BASE64.decode(base64AccessSecret);
        accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(base64RefreshSecret);
        refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    /**
     * 创建accessToken
     * @param subject 用户名
     * @param claims jwt承载数据
     * @return accessToken
     */
    public String createAccessToken(String subject, Map<String, Object> claims) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + accessTokenValidityInMilliseconds);
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(subject)
                .signWith(accessKey, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * 创建refreshToken
     * @param subject 用户名
     * @param claims jwt承载数据
     * @return refreshToken
     */
    public String createRefreshToken(String subject, Map<String, Object> claims) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + refreshTokenValidityInMilliseconds);
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .signWith(refreshKey, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * 通过accessToken获取Authentication
     * @param accessToken jwt
     * @return Authentication
     */
    public Authentication getAuthentication(String accessToken) {
        Claims claims = getClaimsFromAccessToken(accessToken);
        Collection<? extends GrantedAuthority> authorities = jwtUserDetailService.getAuthorities();
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    /**
     * 判断accessToken合法性
     * @param accessToken jwt
     * @return true合法，false不合法
     */
    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, accessKey);
    }

    /**
     * 判断refreshToken合法性
     * @param refreshToken jwt
     * @return true合法，false不合法
     */
    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, refreshKey);
    }

    private boolean validateToken(String token, Key key) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return false;
    }

    /**
     * 获取accessToken有效期
     * @return accessToken有效期
     */
    public Long getAccessTokenValidityInSeconds() {
        return accessTokenValidityInMilliseconds / 1000;
    }

    /**
     * 获取accessToken的承载数据claims
     * @param accessToken jwt
     * @return 承载数据claims
     */
    public static Claims getClaimsFromAccessToken(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(accessToken).getBody();
    }

    /**
     * 获取refreshToken的承载数据claims
     * @param refreshToken jwt
     * @return 承载数据claims
     */
    public static Claims getClaimsFromRefreshToken(String refreshToken) {
        return Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(refreshToken).getBody();
    }

}
