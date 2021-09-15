package ru.yoomoney.tech.moira.client

import com.github.tomakehurst.wiremock.WireMockServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import ru.yoomoney.tech.moira.client.settings.MoiraSettings

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractMoiraTest {

    protected lateinit var server: WireMockServer
    protected lateinit var moira: Moira

    @BeforeAll
    fun beforeAll() {
        server = WireMockServer()
        server.start()
        moira = Moira(MoiraSettings(baseUrl = "http://localhost:${server.port()}/api"))
    }

    @AfterEach
    fun afterEach() {
        server.resetRequests()
        server.resetToDefaultMappings()
    }

    @AfterAll
    fun afterAll() {
        server.stop()
    }
}
