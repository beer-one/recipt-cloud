package com.recipt.gateway.security

import com.recipt.gateway.enums.RedisKeyEnum
import com.recipt.gateway.exception.TokenNotExistException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.util.*

/**
 * @see https://medium.com/@ard333/authentication-and-authorization-using-jwt-on-spring-webflux-29b81f813e78
 */
@Component
class JwtTokenProvider (
    private val jwtTokenProperties: JwtTokenProperties,
    private val stringRedisTemplate: StringRedisTemplate
) {
    fun getAllClaimsFromToken(token: String): Claims = Jwts.parser()
        .setSigningKey(jwtTokenProperties.getSecretKey())
        .parseClaimsJws(token)
        .body

    fun isTokenExpired(claims: Claims) = claims.expiration.before(Date())

    fun validateToken(token: String, email: String) {
        stringRedisTemplate.opsForValue()
            .get(RedisKeyEnum.TOKEN.getKey(email))
            ?.takeIf { it == token }
            ?: throw TokenNotExistException()
    }
}