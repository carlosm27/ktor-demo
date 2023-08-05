package com.example.routes

import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting() {
    route("/user") {
        get {
            if (userStorage.isNotEmpty()) {
                call.respond(userStorage)
            } else {
                call.respondText ("No users found", status = HttpStatusCode.OK)
            }

        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val user =
                userStorage.find { it.id == id } ?: return@get call.respondText(
                    "No user with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(user)
        }
        post {
            val user = call.receive<User>()
            userStorage.add(user)
            call.respondText("User stored correctly", status = HttpStatusCode.Created)

        }
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText(
                "No id provided",
                status = HttpStatusCode.BadRequest
            )
            val user =
                userStorage.find { it.id == id } ?: return@put call.respondText(
                    "No user with this id: $id",
                    status = HttpStatusCode.NotFound
                )
            val newData = call.receive<User>()
            val indexUser = userStorage.indexOf(user)
            userStorage[indexUser] = newData
            call.respondText("User updated", status = HttpStatusCode.OK)
        }
        delete("{id}") {

        }
    }
}