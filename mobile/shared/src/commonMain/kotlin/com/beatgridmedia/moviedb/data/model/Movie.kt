package com.beatgridmedia.moviedb.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val name: String,
    val description: String,
    val year: Int,
    val genres: List<String>,
    val actors: List<String>,
    val directors: List<String>,
    val imageUrl: String,
    val thumbnailUrl: String,
    val rating: Double,
    val duration: String
)
