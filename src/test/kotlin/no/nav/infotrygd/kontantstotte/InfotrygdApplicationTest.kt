package no.nav.infotrygd.kontantstotte

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate
import org.springframework.boot.resttestclient.getForObject
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
class InfotrygdApplicationTest {
    @LocalServerPort
    var port: Int = 0

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun health() {
        testRestTemplate.getForObject<String>("/actuator/health")
    }
}
