package com.beatgridmedia.assignment.dto

import com.beatgridmedia.assignment.entity.Movie

data class MovieSearchResultDto(
    val id: Int,
    val name: String,
    val year: Int,
    val thumbnailUrl: String
) {
    companion object {
        fun from(movie: Movie) = MovieSearchResultDto(
            id = movie.id,
            name = movie.name,
            year = movie.year,
            thumbnailUrl = movie.thumbnailUrl
        )
    }
}
