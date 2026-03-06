package no.nav.infotrygd.kontantstotte

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Profile
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait

fun main(args: Array<String>) {
    val mockOAuth2Server =
        GenericContainer("ghcr.io/navikt/mock-oauth2-server:3.0.1")
            .withNetwork(Network.newNetwork())
            .withNetworkAliases("azuread")
            .withExposedPorts(6969)
            .withEnv(
                mapOf(
                    "SERVER_PORT" to "6969",
                    "TZ" to "Europe/Oslo",
                ),
            ).waitingFor(Wait.forHttp("/default/.well-known/openid-configuration").forStatusCode(200))
            .apply {
                start()
            }
    val port = mockOAuth2Server.getMappedPort(6969)
    val issuerUrl = "http://localhost:$port/default"
    val baseUrl = "http://localhost:$port"

    System.setProperty("AZURE_OPENID_CONFIG_ISSUER", issuerUrl)
    System.setProperty("MOCKSERVER_PORT", "$port")

    SpringApplicationBuilder(ApplicationLocalMock::class.java)
        .profiles("test", "lokal")
        .run(*args)
}

@SpringBootApplication
@Profile("lokal")
class ApplicationLocalMock
