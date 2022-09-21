package no.nav.infotrygd.kontantstotte

import no.nav.infotrygd.kontantstotte.dto.InnsynRequest
import no.nav.infotrygd.kontantstotte.model.ks.Barn
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
import org.springframework.boot.web.server.LocalServerPort
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
        val stonad = sf.stonad()
        stonadRepository.save(stonad)

        val res = testClientFactory.get(port).hentPerioder(InnsynRequest(
            fnr = listOf(stonad.fnr)
        ))

        assertThat(res.data).hasSameSizeAs(listOf(stonad))
    }

    @Test
    fun harKontantstotteIInfotrygd() {
        val sf = StonadFactory()
        val stonad = sf.stonad(barnEksempler = listOf(sf.barn()))
        stonadRepository.save(stonad)

        val res = testClientFactory.get(port).harKontantstotteIInfotrygd(InnsynRequest(
            fnr = listOf(stonad.barn.first().fnr)
        ))

        assertThat(res).isEqualTo(true)
    }

    @Test
    internal fun `hentPerioder noAuth`() {
        val e = assertThrows<TestClientException> { testClientFactory.getNoAuth(port).hentPerioder(InnsynRequest(fnr = listOf(TestData.foedselsNr()))) }
        assertThat(e.status).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    internal fun `hentPerioder feil sub`() {
        val e = assertThrows<TestClientException> { testClientFactory.get(port, sub = "feil").hentPerioder(InnsynRequest(fnr = listOf(TestData.foedselsNr()))) }
        assertThat(e.status).isEqualTo(HttpStatus.FORBIDDEN)
    }
}