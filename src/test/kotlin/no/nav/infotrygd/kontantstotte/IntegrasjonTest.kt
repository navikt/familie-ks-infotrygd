package no.nav.infotrygd.kontantstotte

import no.nav.infotrygd.kontantstotte.dto.InnsynRequest
import no.nav.infotrygd.kontantstotte.repository.StonadRepository
import no.nav.infotrygd.kontantstotte.testutil.StonadFactory
import no.nav.infotrygd.kontantstotte.testutil.TestData
import no.nav.infotrygd.kontantstotte.testutil.rest.TestClientException
import no.nav.infotrygd.kontantstotte.testutil.rest.TestClientFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
internal class IntegrasjonTest {
    @LocalServerPort
    var port: kotlin.Int = 0

    @Autowired
    private lateinit var testClientFactory: TestClientFactory

    @Autowired
    private lateinit var stonadRepository: StonadRepository

    @Test
    fun hentPerioder() {
        val sf = StonadFactory()
        val stonad = sf.stonad(barnEksempler = listOf(sf.barn()))
        stonadRepository.save(stonad)

        val res = testClientFactory.get(port).hentPerioder(
            InnsynRequest(
                barn = listOf(stonad.fnr.asString),
            ),
        )

        assertThat(res.data).hasSameSizeAs(listOf(stonad))
    }

    @Test
    fun hentAlleBarnMedLøpendeFagsakTest() {
        val sf = StonadFactory()
        val barn = sf.barn()
        val utbetaling = sf.utbetaling()
        val stonad = sf.stonad(
            barnEksempler = listOf(barn),
            utbetalingerEksempler = listOf(utbetaling),
            opphoertVfom = "000000",
        )

        stonadRepository.save(stonad)

        val res = testClientFactory.get(port).hentAlleBarnMedLøpendeFagsak()

        assertThat(res).isNotNull
    }

    @Test
    internal fun `hentPerioder noAuth`() {
        val e = assertThrows<TestClientException> { testClientFactory.getNoAuth(port).hentPerioder(InnsynRequest(barn = listOf(TestData.foedselsNr().toString()))) }
        assertThat(e.status).isEqualTo(HttpStatus.UNAUTHORIZED)
    }
}
