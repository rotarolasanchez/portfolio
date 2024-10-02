package com.rotarola

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform