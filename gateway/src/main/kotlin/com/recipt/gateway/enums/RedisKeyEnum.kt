package com.recipt.gateway.enums

enum class RedisKeyEnum(val key: String) {
    TOKEN("recipt:token");

    fun getKey(vararg data: String) = "$key:${data.joinToString(":")}"
}