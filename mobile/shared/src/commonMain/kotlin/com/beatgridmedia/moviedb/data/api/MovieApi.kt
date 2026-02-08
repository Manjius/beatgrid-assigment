package com.beatgridmedia.moviedb.data.api

import com.beatgridmedia.moviedb.data.model.Movie
import com.beatgridmedia.moviedb.data.model.MovieSearchResult
import com.beatgridmedia.moviedb.data.model.RecentSelection
import com.beatgridmedia.moviedb.logging.AppLogger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json

class MovieApi(
    private val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
) : InterfaceMovieApi {
    private val tag = "MovieApi"

    override suspend fun searchMovies(query: String): List<MovieSearchResult> {
        AppLogger.d(tag, "searchMovies started query='$query'")

        return try {
            val results = httpClient.get("${apiBaseUrl()}/api/movies/search") {
                parameter("q", query)
            }.body<List<MovieSearchResult>>()

            AppLogger.d(tag, "searchMovies success query='$query' results=${results.size}")
            results
        } catch (cancellationException: CancellationException) {
            AppLogger.d(tag, "searchMovies cancelled query='$query'")
            throw cancellationException
        } catch (throwable: Throwable) {
            AppLogger.e(
                tag,
                "searchMovies failed query='$query' error='${throwable.message ?: "unknown"}'"
            )
            throw throwable
        }
    }

    override suspend fun getMovie(id: Int): Movie {
        AppLogger.d(tag, "getMovie started id=$id")

        return try {
            val movie = httpClient.get("${apiBaseUrl()}/api/movies/$id").body<Movie>()

            AppLogger.d(tag, "getMovie success id=$id")
            movie
        } catch (cancellationException: CancellationException) {
            AppLogger.d(tag, "getMovie cancelled id=$id")
            throw cancellationException
        } catch (throwable: Throwable) {
            AppLogger.e(tag, "getMovie failed id=$id error='${throwable.message ?: "unknown"}'")
            throw throwable
        }
    }

    override suspend fun selectMovie(id: Int): Movie {
        AppLogger.d(tag, "selectMovie started id=$id")

        return try {
            httpClient.post("${apiBaseUrl()}/api/movies/$id/select")
            val movie = getMovie(id)

            AppLogger.d(tag, "selectMovie success id=$id")
            movie
        } catch (cancellationException: CancellationException) {
            AppLogger.d(tag, "selectMovie cancelled id=$id")
            throw cancellationException
        } catch (throwable: Throwable) {
            AppLogger.e(tag, "selectMovie failed id=$id error='${throwable.message ?: "unknown"}'")
            throw throwable
        }
    }

    override suspend fun getRecentSelections(): List<RecentSelection> {
        TODO("Not yet implemented")
    }
}

expect fun apiBaseUrl(): String
