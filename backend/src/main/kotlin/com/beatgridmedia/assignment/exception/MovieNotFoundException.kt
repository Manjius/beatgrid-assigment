package com.beatgridmedia.assignment.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class MovieNotFoundException(id: Int) : RuntimeException("Movie with id $id not found")
