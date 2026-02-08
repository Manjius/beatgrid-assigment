package com.beatgridmedia.moviedb.presentation

import com.beatgridmedia.moviedb.data.model.MovieSearchResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class IosFrontPageBridge {
    private val presenter = FrontPagePresenter()
    private val observeScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var observationJob: Job? = null

    fun startObserving(onStateChanged: (FrontPageState) -> Unit) {
        observationJob?.cancel()
        observationJob = presenter.state
            .onEach { onStateChanged(it) }
            .launchIn(observeScope)
    }

    fun stopObserving() {
        observationJob?.cancel()
        observationJob = null
    }

    fun onQueryChanged(query: String) = presenter.onQueryChanged(query)

    fun onSuggestionSelected(suggestion: MovieSearchResult) = presenter.onSuggestionSelected(suggestion)

    fun onRecentSelected(movieId: Int) = presenter.onRecentSelected(movieId)

    fun onBack() = presenter.onBack()

    fun clear() {
        stopObserving()
        presenter.clear()
        observeScope.cancel()
    }
}
