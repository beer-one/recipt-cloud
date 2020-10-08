package com.recipt.gateway.exception

enum class GatewayErrorCode(override val code: String): ErrorCode {
    TOKEN_NOT_EXIST("error.gateway.token-not-exist"),
    TOKEN_EXPIRED("error.gateway.token-expired");
}