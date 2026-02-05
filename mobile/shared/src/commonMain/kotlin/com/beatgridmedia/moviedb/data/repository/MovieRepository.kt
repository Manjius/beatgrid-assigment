package com.beatgridmedia.moviedb.data.repository

import com.beatgridmedia.moviedb.data.model.Movie
import com.beatgridmedia.moviedb.data.model.MovieSearchResult
import com.beatgridmedia.moviedb.data.model.RecentSelection

interface MovieRepository {
    suspend fun searchMovies(query: String): Result<List<MovieSearchResult>>
    suspend fun getMovie(id: Int): Result<Movie>
    suspend fun selectMovie(id: Int): Result<Movie>
    suspend fun getRecentSelections(): Result<List<RecentSelection>>
}
