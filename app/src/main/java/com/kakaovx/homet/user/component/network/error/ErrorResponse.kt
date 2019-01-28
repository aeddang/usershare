package com.kakaovx.homet.user.component.network.error

interface ErrorResponse {
    fun errorCode(): Int
    fun errorMessage(): String
    fun kind(): ErrorKind
}