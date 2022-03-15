package com.konna.domain.service

import java.time.LocalDateTime
import java.util.UUID

import org.slf4j.LoggerFactory

import com.konna.application.exception.ResourceNotFoundException
import com.konna.domain.entity.Athlete
import com.konna.domain.entity.Track
import com.konna.domain.entity.Race

interface AthletesService {
    fun findAthlete(id: UUID): Athlete
    fun listAthletes(): List<Athlete>
    fun saveAthlete(athlete: Athlete): Athlete

    fun findTrack(id: UUID): Track
    fun listTracks(): List<Track>
    fun saveTrack(track: Track): Track

    fun findRace(id: UUID): Race
    fun listRaces(): List<Race>
    fun saveRace(race: Race): Race
}

class AthletesServiceImpl(
    val athletes: MutableList<Athlete>,
    val tracks: MutableList<Track>,
    val races: MutableList<Race>
): AthletesService {

    companion object {
        val logger = LoggerFactory.getLogger(AthletesServiceImpl::class.java.simpleName)
    }

    override fun findAthlete(id: UUID): Athlete =
        athletes.find { it.id == id }
            ?: throw ResourceNotFoundException("Athlete with id ${id} not found")

    override fun listAthletes(): List<Athlete> {
        logger.info("Executing action to list athletes")
        return athletes
    }

    override fun saveAthlete(athlete: Athlete): Athlete =
        athlete.takeIf { it.id != null }
            ?.let {
                val id = it.id
                athletes.find { it.id == id }?.let {
                    val updatedAthlete = athlete.copy(
                        createdAt = it.createdAt,
                        updatedAt = LocalDateTime.now()
                    )
                    athletes.remove(it)
                    athletes.add(
                        updatedAthlete
                    )
                    updatedAthlete
                } ?: run {
                    throw ResourceNotFoundException("Athlete with id ${athlete.id} not found")
                }
            } ?: run {
                val newAthlete = athlete.copy(
                    id = UUID.randomUUID()
                )
                athletes.add(
                    newAthlete
                )
                newAthlete
            }

    override fun findTrack(id: UUID): Track =
        tracks.find { it.id == id }
            ?: throw ResourceNotFoundException("Track with id ${id} not found")

    override fun listTracks(): List<Track> = tracks

    override fun saveTrack(track: Track): Track =
        track.takeIf { it.id != null }
            ?.let {
                if(!validateTrack(it)) {
                    throw ResourceNotFoundException("Track athlete with id ${track.athleteId} not found")
                }

                val id = it.id
                tracks.find { it.id == id }?.let {
                    val updatedTrack = track.copy(
                        createdAt = it.createdAt,
                        updatedAt = LocalDateTime.now()
                    )
                    tracks.remove(it)
                    tracks.add(
                        updatedTrack
                    )
                    updatedTrack
                } ?: run {
                    throw ResourceNotFoundException("Track with id ${track.id} not found")
                }
            } ?: run {
                val newTrack = track.copy(
                    id = UUID.randomUUID()
                )
                tracks.add(
                    newTrack
                )
                newTrack
            }

    private fun validateTrack(track: Track): Boolean =
        athletes.find { it.id == track.athleteId }
            ?.let { true } ?: run { false }

    override fun findRace(id: UUID): Race =
        races.find { it.id == id }
            ?: throw ResourceNotFoundException("Race with id ${id} not found")

    override fun listRaces(): List<Race> = races

    override fun saveRace(race: Race): Race =
        race.takeIf { it.id != null }
            ?.let {
                if(!validateRace(it)) {
                    throw ResourceNotFoundException("Race track with id ${race.trackId} not found")
                }

                val id = it.id
                races.find { it.id == id }?.let {
                    val updatedRace = race.copy(
                        createdAt = it.createdAt,
                        updatedAt = LocalDateTime.now()
                    )
                    races.remove(it)
                    races.add(
                        updatedRace
                    )
                    updatedRace
                } ?: run {
                    throw ResourceNotFoundException("Race with id ${race.id} not found")
                }
            } ?: run {
                val newRace = race.copy(
                    id = UUID.randomUUID()
                )
                races.add(
                    newRace
                )
                newRace
            }

    private fun validateRace(race: Race): Boolean =
        tracks.find { it.id == race.trackId }
            ?.let { true } ?: run { false }
}
