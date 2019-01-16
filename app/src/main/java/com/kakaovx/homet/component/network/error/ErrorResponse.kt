package com.kakaovx.homet.component.network.error

interface ErrorResponse {
    fun errorCode(): Int
    fun errorMessage(): String
    fun kind(): ErrorKind
}