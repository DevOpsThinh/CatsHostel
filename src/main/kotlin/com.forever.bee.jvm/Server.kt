/**
 * Start a web server that will respond with OK (http://localhost:8080/status)
 * Contact me: nguyentruongthinhvn2020@gmail.com || +84393280504
 *
 * @author Nguyen Truong Thinh
 * @since 1.6.21
 * */

package com.forever.bee.jvm

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*

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

    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/status") {
            call.respond(mapOf("status" to "OK"))
        }
    }
}