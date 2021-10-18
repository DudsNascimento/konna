/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.konna

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.features.*
import io.ktor.gson.*
import org.koin.logger.slf4jLogger
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.UUID

import org.koin.ktor.ext.*
import org.koin.dsl.module
import org.koin.dsl.single

import com.konna.application.viewmodel.AthleteViewModel
import com.konna.domain.entity.Athlete
import com.konna.application.viewmodel.TrackViewModel
import com.konna.domain.entity.Track
import com.konna.application.viewmodel.RaceViewModel
import com.konna.domain.entity.Race
import com.konna.domain.service.AthletesService
import com.konna.domain.service.AthletesServiceImpl
import com.konna.application.controller.AthletesController
import com.konna.application.controller.AthletesControllerImpl
import com.konna.application.exception.*

fun Application.configureKoin() {
    environment.monitor.subscribe(KoinApplicationStarted) {
        log.info("Koin started.")
    }
    install(Koin) {
        slf4jLogger()
        modules(koinModule)
    }
    environment.monitor.subscribe(KoinApplicationStopPreparing) {
        log.info("Koin stopping...")
    }
    environment.monitor.subscribe(KoinApplicationStopped) {
        log.info("Koin stopped.")
    }
}

fun Application.configureJson() {
    install(ContentNegotiation) {
        gson() {
            registerTypeAdapter(LocalDateTime::class.java, object: TypeAdapter<LocalDateTime>() {
                val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                override fun read(reader: JsonReader): LocalDateTime? {
                    try {
                        return LocalDateTime.parse(reader.nextString(), formatter)
                    } catch(e: DateTimeParseException) {
                        return null
                    }
                }
                override fun write(writer: JsonWriter, value: LocalDateTime?) {
                    value?.let {
                        writer.value(formatter.format(it))
                    } ?: run {
                        writer.nullValue()
                    }
                }
            })
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }
}

fun Application.configureRouting() {
    val athletesController by inject<AthletesController>()
    routing {
        get("") {
            call.respondText("Welcome to Konna!")
        }

        get("/api/athletes/{athleteId}") {
            call.respond(athletesController.findAthlete(
                UUID.fromString(call.parameters["athleteId"]))
            )
        }
        get("/api/athletes") {
            call.respond(athletesController.listAthletes())
        }
        post("/api/athletes") {
            val athlete = call.receive<AthleteViewModel>()
            call.respond(athletesController.saveAthlete(athlete))
        }
        put("/api/athletes") {
            val athlete = call.receive<AthleteViewModel>()
            call.respond(athletesController.saveAthlete(athlete))
        }

        get("/api/tracks/{trackId}") {
            call.respond(athletesController.findTrack(
                UUID.fromString(call.parameters["trackId"]))
            )
        }
        get("/api/tracks") {
            call.respond(athletesController.listTracks())
        }
        post("/api/tracks") {
            val track = call.receive<TrackViewModel>()
            call.respond(athletesController.saveTrack(track))
        }
        put("/api/tracks") {
            val track = call.receive<TrackViewModel>()
            call.respond(athletesController.saveTrack(track))
        }

        get("/api/races/{raceId}") {
            call.respond(athletesController.findRace(
                UUID.fromString(call.parameters["raceId"]))
            )
        }
        get("/api/races") {
            call.respond(athletesController.listRaces())
        }
        post("/api/races") {
            val race = call.receive<RaceViewModel>()
            call.respond(athletesController.saveRace(race))
        }
        put("/api/races") {
            val race = call.receive<RaceViewModel>()
            call.respond(athletesController.saveRace(race))
        }
    }
}

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<ResourceNotFoundException> { cause ->
            call.respond(HttpStatusCode.NotFound, cause.message!!)
        }
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(DefaultHeaders)
        install(CallLogging)
        configureKoin()
        configureJson()
        configureRouting()
        configureErrorHandling()
    }.start(wait = true)
}

val koinModule = module {
    single {
        AthletesControllerImpl(
            get()
        ) as AthletesController
    }
    single {
        AthletesServiceImpl(
            mutableListOf<Athlete>(),
            mutableListOf<Track>(),
            mutableListOf<Race>()
        ) as AthletesService
    }
}
