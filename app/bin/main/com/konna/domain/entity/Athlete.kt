package com.konna.domain.entity

import java.time.LocalDateTime
import java.util.UUID

data class Athlete(
    val id: UUID? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null
)

data class Track(
    val id: UUID? = null,
    val athleteId: UUID,
    val name: String,
    val places: List<DoubleArray>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null
)

data class Race(
    val id: UUID? = null,
    val trackId: UUID,
    val placesTimestamp: List<Long>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null
)
