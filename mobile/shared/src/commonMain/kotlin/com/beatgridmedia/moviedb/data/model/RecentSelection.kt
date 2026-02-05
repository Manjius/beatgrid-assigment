package com.beatgridmedia.moviedb.data.model

data class RecentSelection(
    val movieId: Int,
    val name: String,
    val year: Int,
    val thumbnailUrl: String,
    val selectedAt: String
)
