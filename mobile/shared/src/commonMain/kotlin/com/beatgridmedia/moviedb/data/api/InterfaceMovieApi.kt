package com.beatgridmedia.moviedb.data.api

import com.beatgridmedia.moviedb.data.model.Movie
import com.beatgridmedia.moviedb.data.model.MovieSearchResult
import com.beatgridmedia.moviedb.data.model.RecentSelection

interface InterfaceMovieApi {
    suspend fun searchMovies(query: String): List<MovieSearchResult>
    suspend fun getMovie(id: Int): Movie
    suspend fun selectMovie(id: Int): Movie
    suspend fun getRecentSelections(): List<RecentSelection>
}
