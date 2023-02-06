package com.xxw.platform.plugin.jwt.sdk;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.xxw.platform.plugin.jwt.api.JwtApi;
import com.xxw.platform.plugin.jwt.api.exception.JwtException;
import com.xxw.platform.plugin.jwt.api.exception.enums.JwtExceptionEnum;
import com.xxw.platform.plugin.jwt.api.pojo.config.JwtConfig;
import com.xxw.platform.plugin.jwt.api.pojo.payload.DefaultJwtPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * jwt token的操作对象
 *
 * @author liaoxiting
 * @date 2020/10/31 15:33
 */
public class JwtTokenOperator implements JwtApi {

    private final JwtConfig jwtConfig;

    public JwtTokenOperator(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public String generateToken(Map<String, Object> payload) {

        // 计算过期时间
        DateTime expirationDate = DateUtil.offsetSecond(new Date(), Convert.toInt(jwtConfig.getExpiredSeconds()));

        // 构造jwt token
        return Jwts.builder()
                .setClaims(payload)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getJwtSecret())
                .compact();
    }

    @Override
    public String generateTokenDefaultPayload(DefaultJwtPayload defaultJwtPayload) {

        // 计算过期时间
        DateTime expirationDate = DateUtil.offsetSecond(new Date(), Convert.toInt(jwtConfig.getExpiredSeconds()));

        // 设置过期时间
        defaultJwtPayload.setExpirationDate(expirationDate.getTime());

        // 构造jwt token
        return Jwts.builder()
                .setClaims(BeanUtil.beanToMap(defaultJwtPayload))
                .setSubject(defaultJwtPayload.getUserId().toString())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getJwtSecret())
                .compact();
    }

    @Override
    public Claims getJwtPayloadClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtConfig.getJwtSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public DefaultJwtPayload getDefaultPayload(String token) {
        Map<String, Object> jwtPayload = getJwtPayloadClaims(token);
        return BeanUtil.toBeanIgnoreError(jwtPayload, DefaultJwtPayload.class);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            getJwtPayloadClaims(token);
            return true;
        } catch (io.jsonwebtoken.JwtException jwtException) {
            return false;
        }
    }

    @Override
    public void validateTokenWithException(String token) throws JwtException {

        // 1.先判断是否是token过期了
        boolean tokenIsExpired = this.validateTokenIsExpired(token);
        if (tokenIsExpired) {
            throw new JwtException(JwtExceptionEnum.JWT_EXPIRED_ERROR, token);
        }

        // 2.判断是否是jwt本身的错误
        try {
            getJwtPayloadClaims(token);
        } catch (io.jsonwebtoken.JwtException jwtException) {
            throw new JwtException(JwtExceptionEnum.JWT_PARSE_ERROR, token);
        }
    }

    @Override
    public boolean validateTokenIsExpired(String token) {
        try {
            Claims claims = getJwtPayloadClaims(token);
            final Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            return true;
        }
    }

}
