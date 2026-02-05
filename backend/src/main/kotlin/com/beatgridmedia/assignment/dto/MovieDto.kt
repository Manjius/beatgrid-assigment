package com.beatgridmedia.assignment.dto

import com.beatgridmedia.assignment.entity.Movie

data class MovieDto(
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
) {
    companion object {
        fun from(movie: Movie) = MovieDto(
            id = movie.id,
            name = movie.name,
            description = movie.description,
            year = movie.year,
            genres = movie.genres,
            actors = movie.actors,
            directors = movie.directors,
            imageUrl = movie.imageUrl,
            thumbnailUrl = movie.thumbnailUrl,
            rating = movie.rating,
            duration = movie.duration
        )
    }
}
