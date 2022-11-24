package no.nav.infotrygd.kontantstotte.config

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.*
import no.nav.infotrygd.kontantstotte.Profiles
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
    @Value("\${AUTHORIZATION_URL}")
    val authorizationUrl: String,
    @Value("\${TOKEN_URL}")
    val tokenUrl: String,
    @Value("\${API_SCOPE}")
    val apiScope: String
) {

    @Bean
    fun kontantstøtteInfotrygdApi(): OpenAPI {
        return OpenAPI().info(Info().title("Kontantstøtte infotrygd API").version("v1"))
            .components(Components().addSecuritySchemes("oauth2", securitySchemes()))
            .addSecurityItem(SecurityRequirement().addList("oauth2", listOf("read", "write")))
    }

    private fun securitySchemes(): SecurityScheme {
        return SecurityScheme()
            .name("oauth2")
            .type(SecurityScheme.Type.OAUTH2)
            .scheme("oauth2")
            .`in`(SecurityScheme.In.HEADER)
            .flows(
                OAuthFlows()
                    .authorizationCode(
                        OAuthFlow().authorizationUrl(authorizationUrl)
                            .tokenUrl(tokenUrl)
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
