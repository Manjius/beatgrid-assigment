package com.beatgridmedia.moviedb.presentation

data class FrontPageState(
    val query: String = "",
    val logoTitle: String = "Beatgrid Movies",
    val suggestions: List<String> = emptyList()
) {
    val isDropdownVisible: Boolean
        get() = query.isNotBlank() && suggestions.isNotEmpty()
}

class FrontPageStateHolder {
    private val defaultSuggestion = "the movie goes here"

    fun updateQuery(query: String): FrontPageState {
        val suggestions = if (query.isBlank()) {
            emptyList()
        } else {
            listOf(defaultSuggestion)
        }

        return FrontPageState(
            query = query,
            suggestions = suggestions
        )
    }
}
