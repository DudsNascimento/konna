package com.konna.domain.entity

import java.time.LocalDateTime
import java.util.UUID

data class Athlete(
    val id: UUID? = null,
    val firstName: String,
    val lastName: String,
    val subscribedAt: LocalDateTime
)