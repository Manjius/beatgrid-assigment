package com.beatgridmedia.assignment.dto

import java.time.Instant

data class RecentSelectionDto(
    val movieId: Int,
    val name: String,
    val year: Int,
    val thumbnailUrl: String,
    val selectedAt: Instant
)
