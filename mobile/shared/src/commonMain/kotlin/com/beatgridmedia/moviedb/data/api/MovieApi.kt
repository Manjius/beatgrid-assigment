package com.beatgridmedia.moviedb.data.api

import com.beatgridmedia.moviedb.data.model.Movie
import com.beatgridmedia.moviedb.data.model.MovieSearchResult
import com.beatgridmedia.moviedb.data.model.RecentSelection
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
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
    override suspend fun searchMovies(query: String): List<MovieSearchResult> {
        return httpClient.get("${apiBaseUrl()}/api/movies/search") {
            parameter("q", query)
        }.body()
    }

    override suspend fun getMovie(id: Int): Movie {
        TODO("Not yet implemented")
    }

    override suspend fun selectMovie(id: Int): Movie {
        TODO("Not yet implemented")
    }

    override suspend fun getRecentSelections(): List<RecentSelection> {
        TODO("Not yet implemented")
    }
}

expect fun apiBaseUrl(): String
