package no.nav.infotrygd.kontantstotte

import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.oauth2.sdk.AuthorizationCode
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant
import com.nimbusds.oauth2.sdk.Scope
import com.nimbusds.oauth2.sdk.TokenRequest
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic
import com.nimbusds.oauth2.sdk.auth.Secret
import com.nimbusds.oauth2.sdk.id.ClientID
import no.nav.security.mock.oauth2.OAuth2Config
import no.nav.security.mock.oauth2.token.DefaultOAuth2TokenCallback
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

@RestController
@Profile("lokal")
@RequestMapping("/testtoken")
class TestTokenController(
    @param:Value("\${MOCKSERVER_PORT}") port: Long,
    @param:Value("\${TEAMFAMILIE_FORVALTNING_GROUP_ID}") private val forvalterGroupId: String,
) {
    private val issuerName = "default"
    private val baseEndpointUrl = "http://localhost:$port"

    @PostMapping("/access-as-application-token")
    fun accessToken(): String {
        val issuerUrl = "$baseEndpointUrl/$issuerName"
        val tokenCallback =
            DefaultOAuth2TokenCallback(
                issuerId = "$baseEndpointUrl/$issuerName",
                subject = UUID.randomUUID().toString(),
                typeHeader = JOSEObjectType.JWT.type,
                audience = listOf("test"),
                claims = mapOf("roles" to listOf("access_as_application")),
                expiry = 3600L,
            )

        val tokenRequest =
            TokenRequest(
                URI.create(baseEndpointUrl),
                ClientSecretBasic(ClientID(issuerName), Secret("secret")),
                AuthorizationCodeGrant(AuthorizationCode("12345678901"), URI.create("http://localhost")),
                Scope("test"),
            )
        return OAuth2Config().tokenProvider.accessToken(tokenRequest, issuerUrl.toHttpUrl(), tokenCallback, null).serialize()
    }

    @PostMapping("/forvalter-token")
    fun forvalterToken(): String {
        val issuerUrl = "$baseEndpointUrl/$issuerName"
        val tokenCallback =
            DefaultOAuth2TokenCallback(
                issuerId = "$baseEndpointUrl/$issuerName",
                subject = UUID.randomUUID().toString(),
                typeHeader = JOSEObjectType.JWT.type,
                audience = listOf("test"),
                claims = mapOf("groups" to listOf("$forvalterGroupId")),
                expiry = 3600L,
            )

        val tokenRequest =
            TokenRequest(
                URI.create(baseEndpointUrl),
                ClientSecretBasic(ClientID(issuerName), Secret("secret")),
                AuthorizationCodeGrant(AuthorizationCode("12345678901"), URI.create("http://localhost")),
                Scope("test"),
            )
        return OAuth2Config().tokenProvider.accessToken(tokenRequest, issuerUrl.toHttpUrl(), tokenCallback, null).serialize()
    }
}
