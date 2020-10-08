package com.recipt.gateway.exception

abstract class ReciptException(val errorCode: ErrorCode, vararg args: Any): RuntimeException()

class TokenNotExistException: ReciptException(GatewayErrorCode.TOKEN_NOT_EXIST)
class TokenExpiredException: ReciptException(GatewayErrorCode.TOKEN_EXPIRED)