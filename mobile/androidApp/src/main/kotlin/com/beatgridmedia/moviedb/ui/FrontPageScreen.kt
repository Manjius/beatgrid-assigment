package com.beatgridmedia.moviedb.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.beatgridmedia.moviedb.data.api.InterfaceMovieApi
import com.beatgridmedia.moviedb.data.api.MovieApi
import com.beatgridmedia.moviedb.data.model.Movie
import com.beatgridmedia.moviedb.presentation.FrontPageState
import com.beatgridmedia.moviedb.presentation.FrontPageStateHolder
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun FrontPageScreen(modifier: Modifier = Modifier) {
    val stateHolder = remember { FrontPageStateHolder() }
    val movieApi: InterfaceMovieApi = remember { MovieApi() }
    var uiState by remember { mutableStateOf(FrontPageState()) }
    val coroutineScope = rememberCoroutineScope()

    suspend fun loadRecentSelections() {
        val recents = try {
            movieApi.getRecentSelections()
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (_: Throwable) {
            emptyList()
        }

        uiState = stateHolder.updateRecentSelections(uiState, recents)
    }

    LaunchedEffect(Unit) {
        loadRecentSelections()
    }

    LaunchedEffect(uiState.query) {
        if (uiState.isShowingMovieDetails || uiState.isLoadingMovie) {
            return@LaunchedEffect
        }

        val query = uiState.query.trim()
        if (query.isBlank()) {
            uiState = uiState.copy(suggestions = emptyList())
            return@LaunchedEffect
        }

        delay(500)

        val movies = try {
            movieApi.searchMovies(query)
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (_: Throwable) {
            emptyList()
        }

        if (uiState.query.trim() == query) {
            uiState = stateHolder.updateSuggestions(uiState, movies)
        }
    }

    if (uiState.isShowingMovieDetails) {
        MovieDetailsScreen(
            movie = uiState.selectedMovie!!,
            onGoBack = { uiState = stateHolder.goBack(uiState) },
            modifier = modifier
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = uiState.logoTitle,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.query,
            onValueChange = { query ->
                uiState = stateHolder.updateQuery(uiState, query)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search movies") },
            singleLine = true
        )

        if (uiState.isDropdownVisible) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 4.dp,
                shape = MaterialTheme.shapes.medium
            ) {
                Column {
                    uiState.suggestions.forEach { suggestion ->
                        DropdownMenuItem(
                            text = {
                                Text("${suggestion.name} (${suggestion.year})")
                            },
                            onClick = {
                                uiState = stateHolder.selectSuggestion(uiState, suggestion)
                                coroutineScope.launch {
                                    val selectedMovie = try {
                                        movieApi.selectMovie(suggestion.id)
                                        movieApi.getMovie(suggestion.id)
                                    } catch (cancellationException: CancellationException) {
                                        throw cancellationException
                                    } catch (_: Throwable) {
                                        uiState = stateHolder.movieLoadFailed(uiState)
                                        return@launch
                                    }

                                    uiState = stateHolder.showMovieDetails(uiState, selectedMovie)
                                    loadRecentSelections()
                                }
                            }
                        )
                    }
                }
            }
        }

        if (uiState.isLoadingMovie) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Loading movie details...")
        }

        if (uiState.recentSelections.isNotEmpty()) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Recently Selected",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.recentSelections.chunked(5).forEach { rowRecents ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowRecents.forEach { recent ->
                            Image(
                                painter = rememberAsyncImagePainter(model = recent.thumbnailUrl),
                                contentDescription = recent.name,
                                modifier = Modifier
                                    .size(60.dp, 90.dp)
                                    .clip(MaterialTheme.shapes.small)
                                    .clickable {
                                        uiState = uiState.copy(isLoadingMovie = true, suggestions = emptyList())
                                        coroutineScope.launch {
                                            val movie = try {
                                                movieApi.getMovie(recent.movieId)
                                            } catch (cancellationException: CancellationException) {
                                                throw cancellationException
                                            } catch (_: Throwable) {
                                                uiState = stateHolder.movieLoadFailed(uiState)
                                                return@launch
                                            }

                                            uiState = stateHolder.showMovieDetails(uiState, movie)
                                        }
                                    },
                                contentScale = ContentScale.Crop
                            )
                        }

                        repeat(5 - rowRecents.size) {
                            Spacer(modifier = Modifier.width(60.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieDetailsScreen(movie: Movie, onGoBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = movie.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        Image(
            painter = rememberAsyncImagePainter(model = movie.imageUrl),
            contentDescription = movie.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )

        Text("Description: ${movie.description}")
        Text("Year: ${movie.year}")
        Text("Genres: ${movie.genres.joinToString()}")
        Text("Actors: ${movie.actors.joinToString()}")
        Text("Directors: ${movie.directors.joinToString()}")
        Text("Rating: ${movie.rating}")
        Text("Duration: ${movie.duration}")

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onGoBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go back")
        }
    }
}
