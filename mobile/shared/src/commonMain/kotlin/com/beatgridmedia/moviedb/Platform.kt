package com.beatgridmedia.moviedb

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
