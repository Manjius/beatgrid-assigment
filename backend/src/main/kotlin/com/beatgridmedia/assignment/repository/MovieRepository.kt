package com.beatgridmedia.assignment.repository

import com.beatgridmedia.assignment.entity.Movie
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MovieRepository : JpaRepository<Movie, Int> {
    @Query("SELECT m FROM Movie m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    fun searchByName(@Param("query") query: String): List<Movie>
}
