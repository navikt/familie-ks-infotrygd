package no.nav.infotrygd.kontantstotte

import com.nimbusds.jose.JOSEObjectType
import no.nav.infotrygd.kontantstotte.dto.InnsynRequest
import no.nav.infotrygd.kontantstotte.model.ks.Barn
import no.nav.infotrygd.kontantstotte.repository.StonadRepository
import no.nav.infotrygd.kontantstotte.testutil.StonadFactory
import no.nav.infotrygd.kontantstotte.testutil.TestData
import no.nav.infotrygd.kontantstotte.testutil.rest.TestClientException
import no.nav.infotrygd.kontantstotte.testutil.rest.TestClientFactory
import no.nav.security.mock.oauth2.MockOAuth2Server
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
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
    private lateinit var mockOAuth2Server: MockOAuth2Server

    @Autowired
    private lateinit var stonadRepository: StonadRepository

    @Test
    fun hentPerioder() {
        val sf = StonadFactory()
        val stonad = sf.stonad(barnEksempler = listOf(sf.barn()))
        stonadRepository.save(stonad)

        val res = testClientFactory.get(port).hentPerioder(InnsynRequest(
            barn = listOf(stonad.barn[0].fnr.asString)
        ))

        assertThat(res.data).hasSameSizeAs(listOf(stonad))
    }

    @Test
    fun harKontantstotteIInfotrygd() {
        val sf = StonadFactory()
        val stonad = sf.stonad(barnEksempler = listOf(sf.barn()))
        stonadRepository.save(stonad)

        val res = testClientFactory.get(port).harKontantstotteIInfotrygd(InnsynRequest(
            barn = listOf(stonad.barn.first().fnr.asString)
        ))

        assertThat(res).isEqualTo(true)
    }

    @Test
    internal fun `hentPerioder noAuth`() {
        val e = assertThrows<TestClientException> { testClientFactory.getNoAuth(port).hentPerioder(InnsynRequest(barn = listOf(TestData.foedselsNr().toString()))) }
        assertThat(e.status).isEqualTo(HttpStatus.UNAUTHORIZED)
    }
}


