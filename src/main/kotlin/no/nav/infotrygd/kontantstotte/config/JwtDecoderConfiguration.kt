package no.nav.infotrygd.kontantstotte.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.JdkClientHttpRequestFactory
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.jwt.JwtClaimValidator
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.web.client.RestTemplate
import java.net.InetSocketAddress
import java.net.ProxySelector
import java.net.URI
import java.net.http.HttpClient
import java.time.Duration

@Configuration
class JwtDecoderConfiguration(
    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private val issuerUri: String,
    @Value("\${AZURE_APP_CLIENT_ID}")
    private val clientId: String,
    @Value("\${APP_AZURE_PROXY_URL:}")
    private val proxyUrl: String,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val restTemplate = buildRestTemplate()

        val decoder =
            NimbusJwtDecoder
                .withIssuerLocation(issuerUri)
                .restOperations(restTemplate)
                .build()

        decoder.setJwtValidator(
            DelegatingOAuth2TokenValidator(
                JwtValidators.createDefaultWithIssuer(issuerUri),
                JwtClaimValidator<List<String>>("aud") { aud -> aud.any { it in listOf(clientId, "api://$clientId") } },
            ),
        )

        return decoder
    }

    private fun buildRestTemplate(): RestTemplate {
        val httpClientBuilder =
            HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(2))

        if (proxyUrl.isNotBlank()) {
            val uri = URI.create(proxyUrl)
            logger.info("Konfigurerer JWKS-klient med proxy: ${uri.host}:${uri.port}")
            httpClientBuilder.proxy(ProxySelector.of(InetSocketAddress(uri.host, uri.port)))
        }

        val requestFactory =
            JdkClientHttpRequestFactory(httpClientBuilder.build()).apply {
                setReadTimeout(Duration.ofSeconds(4))
            }

        return RestTemplate(requestFactory)
    }
}
