package no.nav.infotrygd.kontantstotte.testutil

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.support.TestPropertySourceUtils
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait

class MockOAuth2ServerInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    companion object {
        private val mockOAuth2Server: GenericContainer<*> by lazy {
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
                .apply { start() }
        }

        val issuerUrl: String get() {
            val port = mockOAuth2Server.getMappedPort(6969)
            return "http://localhost:$port/default"
        }
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
            applicationContext,
            "AZURE_OPENID_CONFIG_ISSUER=$issuerUrl",
        )
    }
}
