package com.korino.eyedrops

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform