package umfg

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import umfg.application.payloads.CreateUserPayload
import umfg.application.payloads.UpdateUserPayload
import umfg.application.responses.UserCreatedResponse
import umfg.infra.repository.UserRepository

fun Application.configureRouting() {
    routing {
        val repository = UserRepository()
        get("/") {
            call.respondText("Hello World!")
        }
        post("/users") {
            val payload = call.receive<CreateUserPayload>()
            val id = repository.create(payload)
            val response = UserCreatedResponse(id)
            call.respond(HttpStatusCode.Created, response)
        }
        get("/users/{id}") {
            val id: Int = call.parameters["id"]?.toInt()
                ?: throw IllegalArgumentException("ID deve ser informado!")

            val user = repository.findById(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        put("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: throw IllegalArgumentException("ID deve ser informado!")

            val updatedUser = call.receive<UpdateUserPayload>()

            val existingUser = repository.findById(id)
            if (existingUser != null) {
                repository.update(id, updatedUser)
                val updateUser = repository.findById(id)
                if (updateUser != null) {
                    call.respond(HttpStatusCode.OK, "Usuário atualizado com sucesso! Idade: ${updateUser.age} e nome : ${updateUser.name}")
                }
            } else {
                call.respond(HttpStatusCode.NotFound, "Usuário não encontrado!")
            }
        }
        get("/users") {

            val users = repository.findAll()
                call.respond(HttpStatusCode.OK, users)
        }
        delete("/users/{id}") {
            val id: Int = call.parameters["id"]?.toInt()
                ?: throw IllegalArgumentException("ID deve ser informado!")

            val user = repository.delete(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, "usuario deletado")
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

    }
}
