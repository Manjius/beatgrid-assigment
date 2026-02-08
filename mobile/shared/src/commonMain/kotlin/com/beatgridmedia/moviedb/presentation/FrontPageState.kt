package com.beatgridmedia.moviedb.presentation

import com.beatgridmedia.moviedb.data.model.Movie
import com.beatgridmedia.moviedb.data.model.MovieSearchResult

data class FrontPageState(
    val query: String = "",
    val logoTitle: String = "Beatgrid Movies",
    val suggestions: List<MovieSearchResult> = emptyList(),
    val selectedMovie: Movie? = null,
    val isLoadingMovie: Boolean = false
) {
    val isDropdownVisible: Boolean
        get() = query.isNotBlank() && suggestions.isNotEmpty()

    val isShowingMovieDetails: Boolean
        get() = selectedMovie != null
}

class FrontPageStateHolder {
    fun updateQuery(state: FrontPageState, query: String): FrontPageState {
        return state.copy(
            query = query,
            suggestions = if (query.isBlank()) emptyList() else state.suggestions
        )
    }

    fun updateSuggestions(state: FrontPageState, movies: List<MovieSearchResult>): FrontPageState {
        return state.copy(suggestions = movies.take(5))
    }

    fun selectSuggestion(state: FrontPageState, suggestion: MovieSearchResult): FrontPageState {
        return state.copy(
            query = suggestion.name,
            suggestions = emptyList(),
            isLoadingMovie = true
        )
    }

    fun showMovieDetails(state: FrontPageState, movie: Movie): FrontPageState {
        return state.copy(
            selectedMovie = movie,
            isLoadingMovie = false,
            suggestions = emptyList()
        )
    }

    fun movieLoadFailed(state: FrontPageState): FrontPageState {
        return state.copy(isLoadingMovie = false)
    }

    fun goBack(state: FrontPageState): FrontPageState {
        return state.copy(
            selectedMovie = null,
            suggestions = emptyList(),
            isLoadingMovie = false
        )
    }
}
