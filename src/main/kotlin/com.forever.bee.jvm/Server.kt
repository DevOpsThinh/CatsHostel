/**
 * Start a web server that will respond with OK (http://localhost:8080/status)
 * Contact me: nguyentruongthinhvn2020@gmail.com || +84393280504
 *
 * @author Nguyen Truong Thinh
 * @since 1.6.21
 * */

package com.forever.bee.jvm

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

//import kotlinx.html.*
//import io.ktor.server.netty.Netty

//fun HTML.index() {
//    head {
//        title("Hello from Ktor!")
//    }
//    body {
//        div {
//            +"Hello from Ktor"
//        }
//    }
//}

fun main() {
//    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
//        routing {
//            get("/") {
//                call.respondText("OK")
//            }
//        }
//    }.start(wait = true)

    /*
    * Uses server engine: CIO (Coroutine-based I/O)
    * */

    embeddedServer(CIO, port = 8080, module = Application::mainModule).start(wait = true)
}

fun Application.mainModule() {

    /*
     * Connect to the database & create the Cats table
     * */
    DB.connect()
    transaction {
        SchemaUtils.create(CatsTable)
    }

    install(ContentNegotiation) {
        json()
    }

    routing {
        // GET Endpoint
        get("/status") {
            call.respond(mapOf("status" to "OK"))
        }

        cats()

    }
}

private fun Routing.cats() {
   route("/cats") {
       // POST Endpoint
       post {
           call.respond(HttpStatusCode.Created)

           val parameters = call.receiveParameters()
           val name = requireNotNull(parameters["name"])
           val age = parameters["age"]?.toInt() ?: 0

           transaction {
               CatsTable.insert { cat ->
                   cat[CatsTable.name] = name
                   cat[CatsTable.age] = age
               }
           }
       }
       // GET Endpoint
       get {
           val cats = transaction {
               CatsTable.selectAll().map { row ->
                   Cat(
                       row[CatsTable.id].value,
                       row[CatsTable.name],
                       row[CatsTable.age]
                   )
               }
           }
           call.respond(cats)
       }
       get("/{id}") {
           val id = requireNotNull(call.parameters["id"]).toInt()
           val cat = transaction {
               CatsTable.select {
                   CatsTable.id.eq(id)
               }.firstOrNull()
           }

           if (cat == null) {
               call.respond(HttpStatusCode.NotFound)
           } else {
               call.respond(
                   Cat(
                       cat[CatsTable.id].value,
                       cat[CatsTable.name],
                       cat[CatsTable.age]
                   )
               )
           }
       }
   }
}
