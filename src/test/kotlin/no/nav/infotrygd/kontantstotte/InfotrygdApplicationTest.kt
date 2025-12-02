package no.nav.infotrygd.kontantstotte

import no.nav.infotrygd.kontantstotte.testutil.TestContainersConfiguration
import no.nav.infotrygd.kontantstotte.testutil.rest.TestClientFactory
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestContainersConfiguration::class)
class InfotrygdApplicationTest {

    @LocalServerPort
    var port: kotlin.Int = 0

    @Autowired
    private lateinit var testClientFactory: TestClientFactory

    @Test
    fun contextLoads() {
    }

    @Test
    fun health() {
        testClientFactory.getNoAuth(port).health()
    }
}
