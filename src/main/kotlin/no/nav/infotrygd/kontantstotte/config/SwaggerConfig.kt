package no.nav.infotrygd.kontantstotte.config

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.*
import no.nav.infotrygd.kontantstotte.Profiles
import no.nav.security.token.support.core.configuration.MultiIssuerConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

private data class OAuthInfo(
    @JsonProperty("authorization_endpoint")
    val authorizationEndpoint: String,

    @JsonProperty("token_endpoint")
    val tokenEndpoint: String
)

@Configuration
@Profile("!${Profiles.NOAUTH}")
class SwaggerConfig(
    @Value("\${no.nav.security.jwt.issuer.azure.accepted_audience}")
    private val clientId: String,

    private val p: MultiIssuerConfiguration
) {

    private val apiScope: String = "api://${clientId}/.default"

    @Bean
    fun openApi(): OpenAPI {
        val metaData = p.getIssuer("azure").get().metaData

        val info = OAuthInfo(
            authorizationEndpoint = metaData.authorizationEndpointURI.toString(),
            tokenEndpoint = metaData.tokenEndpointURI.toString()
        )

        return OpenAPI().info(Info().title("KS Infotrygd API (v2)"))
            .components(
                Components()
                    .addSecuritySchemes("oauth2", oauth2SecurityScheme(info))
                    .addSecuritySchemes("bearer", bearerTokenSecurityScheme())
            )
            .addSecurityItem(SecurityRequirement().addList("oauth2", listOf("read", "write")))
            .addSecurityItem(SecurityRequirement().addList("bearer", listOf("read", "write")))
    }

    private fun oauth2SecurityScheme(info: OAuthInfo): SecurityScheme {
        return SecurityScheme()
            .name("oauth2")
            .type(SecurityScheme.Type.OAUTH2)
            .scheme("oauth2")
            .`in`(SecurityScheme.In.HEADER)
            .flows(
                OAuthFlows()
                    .authorizationCode(
                        OAuthFlow().authorizationUrl(info.authorizationEndpoint)
                            .tokenUrl(info.tokenEndpoint)
                            .scopes(Scopes().addString(apiScope, "read,write"))
                    )
            )

    }

    private fun bearerTokenSecurityScheme(): SecurityScheme {
        return SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .scheme("bearer")
            .bearerFormat("JWT")
            .`in`(SecurityScheme.In.HEADER)
            .name("Authorization")
    }

}
