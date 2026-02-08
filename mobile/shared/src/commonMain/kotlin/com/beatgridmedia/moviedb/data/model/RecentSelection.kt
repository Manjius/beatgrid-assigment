package com.beatgridmedia.moviedb.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RecentSelection(
    val movieId: Int,
    val name: String,
    val year: Int,
    val thumbnailUrl: String,
    val selectedAt: String
)
