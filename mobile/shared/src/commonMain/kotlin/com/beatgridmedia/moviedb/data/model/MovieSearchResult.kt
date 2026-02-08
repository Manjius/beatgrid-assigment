package com.beatgridmedia.moviedb.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieSearchResult(
    val id: Int,
    val name: String,
    val year: Int,
    val thumbnailUrl: String
)
