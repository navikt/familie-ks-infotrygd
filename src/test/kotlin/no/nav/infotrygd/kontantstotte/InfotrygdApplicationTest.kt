package no.nav.infotrygd.kontantstotte

import no.nav.infotrygd.kontantstotte.testutil.AbstractStonadFactoryTest
import no.nav.infotrygd.kontantstotte.testutil.MockOAuth2ServerInitializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate
import org.springframework.boot.resttestclient.getForObject
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
@ContextConfiguration(initializers = [MockOAuth2ServerInitializer::class])
class InfotrygdApplicationTest : AbstractStonadFactoryTest() {
    @LocalServerPort
    var port: Int = 0

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun health() {
        testRestTemplate.getForObject<String>("/actuator/health")
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "/internal/info",
            "/internal/mappings",
            "/internal/status/isAlive",
            "/internal/healthcheck",
            "/internal/prometheus/other",
        ],
    )
    fun `skal avvise øvrige internal-endepunkter uten token`(path: String) {
        val response = testRestTemplate.getForEntity(path, String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }
}
