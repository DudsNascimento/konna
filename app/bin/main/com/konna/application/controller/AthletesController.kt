package com.konna.application.controller

import java.time.LocalDateTime
import java.util.UUID

import com.konna.application.exception.ResourceNotFoundException
import com.konna.application.viewmodel.AthleteViewModel
import com.konna.application.viewmodel.toAthleteViewModel
import com.konna.application.viewmodel.toAthlete
import com.konna.application.viewmodel.TrackViewModel
import com.konna.application.viewmodel.toTrackViewModel
import com.konna.application.viewmodel.toTrack
import com.konna.application.viewmodel.RaceViewModel
import com.konna.application.viewmodel.toRaceViewModel
import com.konna.application.viewmodel.toRace
import com.konna.domain.service.AthletesService
import com.konna.domain.entity.Athlete

interface AthletesController {
    fun findAthlete(id: UUID): AthleteViewModel
    fun listAthletes(): List<AthleteViewModel>
    fun saveAthlete(athlete: AthleteViewModel): AthleteViewModel

    fun findTrack(id: UUID): TrackViewModel
    fun listTracks(): List<TrackViewModel>
    fun saveTrack(track: TrackViewModel): TrackViewModel

    fun findRace(id: UUID): RaceViewModel
    fun listRaces(): List<RaceViewModel>
    fun saveRace(race: RaceViewModel): RaceViewModel
}

class AthletesControllerImpl(val athletesService: AthletesService): AthletesController {

    override fun findAthlete(id: UUID): AthleteViewModel =
        athletesService.findAthlete(id)
            .toAthleteViewModel()

    override fun listAthletes(): List<AthleteViewModel> =
        athletesService.listAthletes().map {
            it.toAthleteViewModel()
        }
        
    override fun saveAthlete(athlete: AthleteViewModel): AthleteViewModel =
        athletesService.saveAthlete(athlete.toAthlete())
            .toAthleteViewModel()

    override fun findTrack(id: UUID): TrackViewModel =
        athletesService.findTrack(id)
            .toTrackViewModel()

    override fun listTracks(): List<TrackViewModel> =
        athletesService.listTracks().map {
            it.toTrackViewModel()
        }
        
    override fun saveTrack(track: TrackViewModel): TrackViewModel =
        athletesService.saveTrack(track.toTrack())
            .toTrackViewModel()

    override fun findRace(id: UUID): RaceViewModel =
        athletesService.findRace(id)
            .toRaceViewModel()

    override fun listRaces(): List<RaceViewModel> =
        athletesService.listRaces().map {
            it.toRaceViewModel()
        }
        
    override fun saveRace(race: RaceViewModel): RaceViewModel =
        athletesService.saveRace(race.toRace())
            .toRaceViewModel()
}
