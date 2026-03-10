package no.nav.infotrygd.kontantstotte.security

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.jwt.Jwt
import java.time.Instant

class AzureJwtAuthenticationConverterTest {
    private val forvalterGroupId = "c7e0b108-7ae6-432c-9ab4-946174c240c0"
    private val converter = AzureJwtAuthenticationConverter(forvalterGroupId)

    @Test
    fun `roles-claim med access_as_application gir ROLE_APPLICATION`() {
        val jwt = buildJwt(roles = listOf(ACCESS_AS_APPLICATION_ROLE))

        val token = converter.convert(jwt)!!

        assertThat(token.authorities.map { it.authority }).containsExactly("ROLE_APPLICATION")
    }

    @Test
    fun `groups-claim med forvalterGroupId gir ROLE_FORVALTER`() {
        val jwt = buildJwt(groups = listOf(forvalterGroupId))

        val token = converter.convert(jwt)!!

        assertThat(token.authorities.map { it.authority }).containsExactly("ROLE_FORVALTER")
    }

    @Test
    fun `begge gyldige claims gir begge roller`() {
        val jwt = buildJwt(roles = listOf(ACCESS_AS_APPLICATION_ROLE), groups = listOf(forvalterGroupId))

        val token = converter.convert(jwt)!!

        assertThat(token.authorities.map { it.authority }).containsExactlyInAnyOrder("ROLE_APPLICATION", "ROLE_FORVALTER")
    }

    @Test
    fun `ukjent group og ingen roller returnerer ingen roller`() {
        val jwt = buildJwt(groups = listOf("ukjent-gruppe"))

        val token = converter.convert(jwt)!!

        assertThat(token.authorities).isEmpty()
    }

    @Test
    fun `tom token uten claims returnerer ingen roller`() {
        val jwt = buildJwt()

        val token = converter.convert(jwt)!!

        assertThat(token.authorities).isEmpty()
    }

    private fun buildJwt(
        roles: List<String> = emptyList(),
        groups: List<String> = emptyList(),
    ): Jwt {
        val claims = mutableMapOf<String, Any>("sub" to "test-subject")
        if (roles.isNotEmpty()) claims["roles"] = roles
        if (groups.isNotEmpty()) claims["groups"] = groups

        return Jwt
            .withTokenValue("test-token")
            .header("alg", "RS256")
            .claims { it.putAll(claims) }
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600))
            .build()
    }
}
