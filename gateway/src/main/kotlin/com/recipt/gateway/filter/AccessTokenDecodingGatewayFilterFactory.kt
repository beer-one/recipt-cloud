package com.recipt.gateway.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.recipt.gateway.constants.ReciptAttributes.MEMBER_INFO
import com.recipt.gateway.constants.ReciptHeaders.AUTH_TOKEN
import com.recipt.gateway.exception.TokenExpiredException
import com.recipt.gateway.exception.TokenNotExistException
import com.recipt.gateway.security.JwtTokenProvider
import com.recipt.gateway.security.MemberInfo
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component

@Component
class AccessTokenDecodingGatewayFilterFactory(
    private val jwtTokenProvider: JwtTokenProvider,
    private val objectMapper: ObjectMapper
): AbstractGatewayFilterFactory<Any>() {

    override fun apply(config: Any?): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            exchange.request.headers[AUTH_TOKEN]?.firstOrNull()?.let { token ->
                val claims = jwtTokenProvider.getAllClaimsFromToken(token)
                if (jwtTokenProvider.isTokenExpired(claims)) throw TokenExpiredException()

                val memberInfo = claims.get(MEMBER_INFO, String::class.java)
                    .let { objectMapper.readValue(it, MemberInfo::class.java) }

                exchange.attributes[MEMBER_INFO] = memberInfo
            }?: throw TokenNotExistException()

            chain.filter(exchange)
        }
    }
}