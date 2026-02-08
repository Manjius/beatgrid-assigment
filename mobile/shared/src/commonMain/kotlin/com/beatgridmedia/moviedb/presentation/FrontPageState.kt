package com.beatgridmedia.moviedb.presentation

import com.beatgridmedia.moviedb.data.model.MovieSearchResult

data class FrontPageState(
    val query: String = "",
    val logoTitle: String = "Beatgrid Movies",
    val suggestions: List<String> = emptyList()
) {
    val isDropdownVisible: Boolean
        get() = query.isNotBlank() && suggestions.isNotEmpty()
}

class FrontPageStateHolder {
    fun updateQuery(state: FrontPageState, query: String): FrontPageState {
        return state.copy(
            query = query,
            suggestions = if (query.isBlank()) emptyList() else state.suggestions
        )
    }

    fun updateSuggestions(state: FrontPageState, movies: List<MovieSearchResult>): FrontPageState {
        return state.copy(suggestions = movies.take(5).map { it.name })
    }

    fun selectSuggestion(state: FrontPageState, suggestion: String): FrontPageState {
        return state.copy(
            query = suggestion,
            suggestions = listOf(suggestion)
        )
    }
}
