package com.beatgridmedia.assignment.controller

import com.beatgridmedia.assignment.dto.MovieDto
import com.beatgridmedia.assignment.dto.MovieSearchResultDto
import com.beatgridmedia.assignment.dto.RecentSelectionDto
import com.beatgridmedia.assignment.service.MovieService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/movies")
class MovieController(
    private val movieService: MovieService
) {
    @GetMapping("/search")
    fun searchMovies(@RequestParam q: String): ResponseEntity<List<MovieSearchResultDto>> {
        return ResponseEntity.ok(movieService.searchMovies(q))
    }

    @GetMapping("/{id}")
    fun getMovie(@PathVariable id: Int): ResponseEntity<MovieDto> {
        return ResponseEntity.ok(movieService.getMovieById(id))
    }

    @PostMapping("/{id}/select")
    fun selectMovie(@PathVariable id: Int): ResponseEntity<MovieDto> {
        return ResponseEntity.ok(movieService.selectMovie(id))
    }

    @GetMapping("/recent")
    fun getRecentSelections(): ResponseEntity<List<RecentSelectionDto>> {
        return ResponseEntity.ok(movieService.getRecentSelections())
    }
}
