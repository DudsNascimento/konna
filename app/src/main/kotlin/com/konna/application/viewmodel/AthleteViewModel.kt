package com.konna.application.viewmodel

import java.time.LocalDateTime
import java.util.UUID

import com.konna.domain.entity.Athlete
import com.konna.domain.entity.Track
import com.konna.domain.entity.Race

data class AthleteViewModel(
    val id: UUID? = null,
    val email: String,
    val firstName: String,
    val lastName: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

fun AthleteViewModel.toAthlete() = Athlete(
    id = id,
    email = email,
    firstName = firstName,
    lastName = lastName,
    createdAt = createdAt ?: LocalDateTime.now(),
    updatedAt = updatedAt
)

fun Athlete.toAthleteViewModel() = AthleteViewModel(
    id = id,
    email = email,
    firstName = firstName,
    lastName = lastName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

data class TrackViewModel(
    val id: UUID? = null,
    val athleteId: UUID,
    val name: String,
    val places: List<DoubleArray>,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

fun TrackViewModel.toTrack() = Track(
    id = id,
    athleteId = athleteId,
    name = name,
    places = places,
    createdAt = createdAt ?: LocalDateTime.now(),
    updatedAt = updatedAt
)

fun Track.toTrackViewModel() = TrackViewModel(
    id = id,
    athleteId = athleteId,
    name = name,
    places = places,
    createdAt = createdAt,
    updatedAt = updatedAt
)

data class RaceViewModel(
    val id: UUID? = null,
    val trackId: UUID,
    val placesTimestamp: List<Long>,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)

fun RaceViewModel.toRace() = Race(
    id = id,
    trackId = trackId,
    placesTimestamp = placesTimestamp,
    createdAt = createdAt ?: LocalDateTime.now(),
    updatedAt = updatedAt
)

fun Race.toRaceViewModel() = RaceViewModel(
    id = id,
    trackId = trackId,
    placesTimestamp = placesTimestamp,
    createdAt = createdAt,
    updatedAt = updatedAt
)
