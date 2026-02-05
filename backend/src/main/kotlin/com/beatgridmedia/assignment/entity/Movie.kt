package com.beatgridmedia.assignment.entity

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.*

@Entity
@Table(name = "movie")
class Movie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false)
    val name: String = "",

    @Column(nullable = false)
    val description: String = "",

    @Column(nullable = false)
    val year: Int = 0,

    @Column(nullable = false, columnDefinition = "JSON")
    @Convert(converter = StringListConverter::class)
    val genres: List<String> = emptyList(),

    @Column(nullable = false, columnDefinition = "JSON")
    @Convert(converter = StringListConverter::class)
    val actors: List<String> = emptyList(),

    @Column(nullable = false, columnDefinition = "JSON")
    @Convert(converter = StringListConverter::class)
    val directors: List<String> = emptyList(),

    @Column(name = "image_url", nullable = false)
    val imageUrl: String = "",

    @Column(name = "thumbnail_url", nullable = false)
    val thumbnailUrl: String = "",

    @Column(nullable = false)
    val rating: Double = 0.0,

    @Column(nullable = false)
    val duration: String = ""
)

@Converter
class StringListConverter : AttributeConverter<List<String>, String> {
    private val objectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(attribute: List<String>?): String {
        return objectMapper.writeValueAsString(attribute ?: emptyList<String>())
    }

    override fun convertToEntityAttribute(dbData: String?): List<String> {
        if (dbData.isNullOrEmpty()) return emptyList()
        return objectMapper.readValue(dbData, object : TypeReference<List<String>>() {})
    }
}
