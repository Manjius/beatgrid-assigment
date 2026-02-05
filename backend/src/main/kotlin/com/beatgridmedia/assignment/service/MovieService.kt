package com.beatgridmedia.assignment.service

import com.beatgridmedia.assignment.config.ApiConfig
import com.beatgridmedia.assignment.dto.MovieDto
import com.beatgridmedia.assignment.dto.MovieSearchResultDto
import com.beatgridmedia.assignment.dto.RecentSelectionDto
import com.beatgridmedia.assignment.entity.Movie
import com.beatgridmedia.assignment.exception.MovieNotFoundException
import com.beatgridmedia.assignment.repository.MovieRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.concurrent.ConcurrentLinkedDeque

@Service
class MovieService(
    private val movieRepository: MovieRepository,
    private val apiConfig: ApiConfig
) {
    private data class Selection(val movieId: Int, val timestamp: Instant)

    private val recentSelections = ConcurrentLinkedDeque<Selection>()
    private val maxRecentSelections = 10

    fun searchMovies(query: String): List<MovieSearchResultDto> {
        if (query.isBlank()) return emptyList()
        return movieRepository.searchByName(query.trim())
            .map { MovieSearchResultDto.from(it) }
    }

    fun getMovieById(id: Int): MovieDto {
        val movie = movieRepository.findById(id)
            .orElseThrow { MovieNotFoundException(id) }
        return toMovieDto(movie)
    }

    fun selectMovie(id: Int): MovieDto {
        val movie = movieRepository.findById(id)
            .orElseThrow { MovieNotFoundException(id) }

        // Remove any existing selection for this movie
        recentSelections.removeIf { it.movieId == id }

        // Add new selection at the front
        recentSelections.addFirst(Selection(id, Instant.now()))

        // Trim to max size
        while (recentSelections.size > maxRecentSelections) {
            recentSelections.removeLast()
        }

        return toMovieDto(movie)
    }

    fun getRecentSelections(): List<RecentSelectionDto> {
        val movieIds = recentSelections.map { it.movieId }
        val movies = movieRepository.findAllById(movieIds).associateBy { it.id }

        return recentSelections.mapNotNull { selection ->
            movies[selection.movieId]?.let { movie ->
                RecentSelectionDto(
                    movieId = movie.id,
                    name = movie.name,
                    year = movie.year,
                    thumbnailUrl = movie.thumbnailUrl,
                    selectedAt = selection.timestamp
                )
            }
        }
    }

    private fun toMovieDto(movie: Movie): MovieDto {
        val genres = if (apiConfig.normalizeGenres) {
            movie.genres.map { normalizeGenre(it) }
        } else {
            movie.genres
        }

        return MovieDto(
            id = movie.id,
            name = movie.name,
            description = movie.description,
            year = movie.year,
            genres = genres,
            actors = movie.actors,
            directors = movie.directors,
            imageUrl = movie.imageUrl,
            thumbnailUrl = movie.thumbnailUrl,
            rating = movie.rating,
            duration = movie.duration
        )
    }

    private fun normalizeGenre(genre: String): String {
        return genre.replace("-", "")
    }
}
