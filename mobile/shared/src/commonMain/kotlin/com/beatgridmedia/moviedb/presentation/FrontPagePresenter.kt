package com.beatgridmedia.moviedb.presentation

import com.beatgridmedia.moviedb.data.api.InterfaceMovieApi
import com.beatgridmedia.moviedb.data.api.MovieApi
import com.beatgridmedia.moviedb.data.model.MovieSearchResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FrontPagePresenter(
    private val movieApi: InterfaceMovieApi = MovieApi(),
    private val stateHolder: FrontPageStateHolder = FrontPageStateHolder(),
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {
    private val _state = MutableStateFlow(FrontPageState())
    val state: StateFlow<FrontPageState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        refreshRecentSelections()
    }

    fun onQueryChanged(query: String) {
        _state.value = stateHolder.updateQuery(_state.value, query)
        searchJob?.cancel()

        val trimmedQuery = query.trim()
        if (trimmedQuery.isBlank() || _state.value.isShowingMovieDetails || _state.value.isLoadingMovie) {
            return
        }

        searchJob = coroutineScope.launch {
            delay(500)
            val movies = runCatching { movieApi.searchMovies(trimmedQuery) }
                .getOrElse { throwable ->
                    if (throwable is CancellationException) {
                        throw throwable
                    }

                    if (_state.value.query.trim() == trimmedQuery) {
                        _state.value = stateHolder.searchUnavailable(_state.value)
                    }
                    return@launch
                }

            if (_state.value.query.trim() == trimmedQuery) {
                _state.value = stateHolder.updateSuggestions(_state.value, movies)
            }
        }
    }

    fun onSuggestionSelected(suggestion: MovieSearchResult) {
        _state.value = stateHolder.selectSuggestion(_state.value, suggestion)

        coroutineScope.launch {
            val movie = runCatching {
                movieApi.selectMovie(suggestion.id)
                movieApi.getMovie(suggestion.id)
            }.getOrElse { throwable ->
                if (throwable is CancellationException) {
                    throw throwable
                }

                _state.value = stateHolder.movieLoadFailed(_state.value)
                return@launch
            }

            _state.value = stateHolder.showMovieDetails(_state.value, movie)
            refreshRecentSelections()
        }
    }

    fun onRecentSelected(movieId: Int) {
        _state.value = _state.value.copy(isLoadingMovie = true, suggestions = emptyList())

        coroutineScope.launch {
            val movie = runCatching { movieApi.getMovie(movieId) }
                .getOrElse { throwable ->
                    if (throwable is CancellationException) {
                        throw throwable
                    }

                    _state.value = stateHolder.movieLoadFailed(_state.value)
                    return@launch
                }

            _state.value = stateHolder.showMovieDetails(_state.value, movie)
        }
    }

    fun onBack() {
        _state.value = stateHolder.goBack(_state.value)
    }

    fun clear() {
        searchJob?.cancel()
        coroutineScope.cancel()
    }

    private fun refreshRecentSelections() {
        coroutineScope.launch {
            val recents = runCatching { movieApi.getRecentSelections() }
                .getOrElse { throwable ->
                    if (throwable is CancellationException) {
                        throw throwable
                    }

                    _state.value = stateHolder.recentsUnavailable(_state.value)
                    return@launch
                }

            _state.value = stateHolder.updateRecentSelections(_state.value, recents)
        }
    }
}
