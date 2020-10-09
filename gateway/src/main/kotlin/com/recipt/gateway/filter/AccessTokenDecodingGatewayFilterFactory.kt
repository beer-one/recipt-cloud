package com.recipt.gateway.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.recipt.gateway.constants.ReciptAttributes.MEMBER_INFO
import com.recipt.gateway.constants.ReciptHeaders.AUTH_TOKEN
import com.recipt.gateway.constants.ReciptHeaders.MEMBER_INFO_HEADER
import com.recipt.gateway.exception.TokenExpiredException
import com.recipt.gateway.exception.TokenNotExistException
import com.recipt.gateway.security.JwtTokenProvider
import com.recipt.gateway.security.MemberInfo
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange

@Component
class AccessTokenDecodingGatewayFilterFactory(
    private val jwtTokenProvider: JwtTokenProvider
): AbstractGatewayFilterFactory<Any>() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun apply(config: Any?): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val request = exchange.request

            val memberInfoString = request.headers[AUTH_TOKEN]?.firstOrNull()?.let { token ->
                val claims = jwtTokenProvider.getAllClaimsFromToken(token)
                if (jwtTokenProvider.isTokenExpired(claims)) throw TokenExpiredException()

                claims.get(MEMBER_INFO, String::class.java)
                    .also { logger.info("MEMBER_INFO: $it")}
            }?: throw TokenNotExistException()

            chain.filter(exchange.mutate()
                .request(request.mutate()
                    .headers { header ->
                        header.add(MEMBER_INFO_HEADER, memberInfoString)
                    }.build()
                ).build()
            )
        }
    }
}