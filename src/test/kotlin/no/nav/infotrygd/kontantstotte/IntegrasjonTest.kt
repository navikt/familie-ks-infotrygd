package no.nav.infotrygd.kontantstotte

import no.nav.infotrygd.kontantstotte.config.ACCESS_AS_APPLICATION_ROLE
import no.nav.infotrygd.kontantstotte.dto.InnsynRequest
import no.nav.infotrygd.kontantstotte.dto.InnsynResponse
import no.nav.infotrygd.kontantstotte.repository.StonadRepository
import no.nav.infotrygd.kontantstotte.testutil.AbstractStonadFactoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import tools.jackson.module.kotlin.jsonMapper
import tools.jackson.module.kotlin.readValue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
internal class IntegrasjonTest : AbstractStonadFactoryTest() {
    @Autowired
    private lateinit var stonadRepository: StonadRepository

    @Autowired private lateinit var mockMvc: MockMvc

    @Test
    fun `skal hente perioder med token med rolle access_as_application`() {
        val stonad = sf.stonad(barnEksempler = listOf(sf.barn()))
        stonadRepository.save(stonad)

        val result =
            mockMvc
                .post("/api/hentPerioderMedKontantstøtteIInfotrygd") {
                    with(
                        jwt()
                            .jwt {
                                it.claim(
                                    "roles",
                                    listOf(ACCESS_AS_APPLICATION_ROLE),
                                )
                            }.authorities(SimpleGrantedAuthority("ROLE_APPLICATION")),
                    )
                    contentType = MediaType.APPLICATION_JSON
                    content = jsonMapper().writeValueAsString(InnsynRequest(barn = listOf(stonad.fnr.asString)))
                }.andExpect {
                    status { isOk() }
                }.andReturn()

        val response: InnsynResponse = jsonMapper().readValue(result.response.contentAsString)
        assertThat(response.data).hasSameSizeAs(listOf(stonad))
    }

    @Test
    fun `skal hente perioder med token med forvalterRolle`() {
        val stonad = sf.stonad(barnEksempler = listOf(sf.barn()))
        stonadRepository.save(stonad)

        val result =
            mockMvc
                .post("/api/hentPerioderMedKontantstøtteIInfotrygd") {
                    with(
                        jwt()
                            .jwt {
                                it.claim(
                                    "groups",
                                    listOf("c7e0b108-7ae6-432c-9ab4-946174c240c0"),
                                )
                            }.authorities(SimpleGrantedAuthority("ROLE_FORVALTER")),
                    )
                    contentType = MediaType.APPLICATION_JSON
                    content = jsonMapper().writeValueAsString(InnsynRequest(barn = listOf(stonad.fnr.asString)))
                }.andExpect {
                    status { isOk() }
                }.andReturn()

        val response: InnsynResponse = jsonMapper().readValue(result.response.contentAsString)
        assertThat(response.data).hasSameSizeAs(listOf(stonad))
    }

    @Test
    fun `skal få forbidden hvis man mangler tilgang`() {
        val stonad = sf.stonad(barnEksempler = listOf(sf.barn()))
        stonadRepository.save(stonad)

        mockMvc
            .post("/api/hentPerioderMedKontantstøtteIInfotrygd") {
                with(jwt().jwt { it.claim("groups", listOf("ikke_tilgang")) })
                contentType = MediaType.APPLICATION_JSON
                content = jsonMapper().writeValueAsString(InnsynRequest(barn = listOf(stonad.fnr.asString)))
            }.andExpect {
                status { isForbidden() }
            }
    }

    @Test
    fun `skal få unauthorized hvis man mangler token`() {
        val stonad = sf.stonad(barnEksempler = listOf(sf.barn()))
        stonadRepository.save(stonad)

        mockMvc
            .post("/api/hentPerioderMedKontantstøtteIInfotrygd") {
                contentType = MediaType.APPLICATION_JSON
                content = jsonMapper().writeValueAsString(InnsynRequest(barn = listOf(stonad.fnr.asString)))
            }.andExpect {
                status { isUnauthorized() }
            }
    }

    @Test
    fun `Skal hente alle identer med løpende sak`() {
        val barn = sf.barn()
        val utbetaling = sf.utbetaling()
        val stonad =
            sf.stonad(
                barnEksempler = listOf(barn),
                utbetalingerEksempler = listOf(utbetaling),
                opphoertVfom = "000000",
            )

        stonadRepository.save(stonad)

        val result =
            mockMvc
                .get("/api/hentidentertilbarnmedlopendesaker") {
                    with(
                        jwt()
                            .jwt {
                                it.claim("roles", listOf(ACCESS_AS_APPLICATION_ROLE))
                            }.authorities(SimpleGrantedAuthority("ROLE_APPLICATION")),
                    )
                }.andExpect {
                    status { isOk() }
                }.andReturn()

        val response: List<String> = jsonMapper().readValue(result.response.contentAsString)

        assertThat(response).hasSize(1)
    }
}
