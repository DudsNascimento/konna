package com.konna.application.controller

import java.time.LocalDateTime
import java.util.UUID

import com.konna.application.exception.ResourceNotFoundException
import com.konna.domain.entity.Athlete

interface AthletesController {
    fun listAthletes(): List<Athlete>
    fun saveAthlete(athlete: Athlete): Athlete
}

class AthletesControllerImpl(val athletes: MutableList<Athlete>): AthletesController {

    override fun listAthletes(): List<Athlete> = athletes
    override fun saveAthlete(athlete: Athlete): Athlete =
        athlete.takeIf { it.id != null }
            ?.let {
                val id = it.id
                athletes.find { it.id == id }?.let {
                    athletes.remove(it)
                    athletes.add(athlete)
                } ?: run {
                    throw ResourceNotFoundException("Athlete with id ${athlete.id} not found")
                }
                athlete
            } ?: run {
                val newAthlete = athlete.copy(
                    id = UUID.randomUUID()
                )
                athletes.add(
                    newAthlete
                )
                newAthlete
            }
}
