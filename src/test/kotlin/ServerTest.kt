/**
 * Testing the service
 * Contact me: nguyentruongthinhvn2020@gmail.com || +84393280504
 *
 * @author Nguyen Truong Thinh
 * @since 1.6.21
 * */

import com.forever.bee.jvm.mainModule
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals

internal class ServerTest {
    @Test
    fun testStatus() {
        withTestApplication(Application::mainModule) {
            val response = handleRequest(HttpMethod.Get, "/status").response
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("""{"status":"OK"}""", response.content)
        }
    }
}