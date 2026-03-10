package no.nav.infotrygd.kontantstotte.config

import no.nav.infotrygd.kontantstotte.security.AzureJwtAuthenticationConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.client.RestTemplate
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.net.InetSocketAddress
import java.net.Proxy

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
open class SecurityConfiguration(
    private val azureJwtAuthenticationConverter: AzureJwtAuthenticationConverter,
    private val environment: Environment,
    @Value("\${AZURE_OPENID_CONFIG_ISSUER}") private val issuerUri: String,
) {
    @Bean
    open fun jwtDecoder(): JwtDecoder {
        val restTemplate =
            if (environment.activeProfiles.any { it in listOf("prod", "preprod") }) {
                val factory =
                    SimpleClientHttpRequestFactory().apply {
                        setProxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("webproxy-nais.nav.no", 8088)))
                    }
                RestTemplate(factory)
            } else {
                RestTemplate()
            }

        return NimbusJwtDecoder
            .withIssuerLocation(issuerUri)
            .restOperations(restTemplate)
            .build()
    }

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            cors { configurationSource = corsConfigurationSource() }
            authorizeHttpRequests {
                authorize("/internal/**", permitAll)
                authorize("/actuator/**", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/swagger-ui.html", permitAll)
                authorize("/tables", permitAll)
                authorize("/testtoken/**", permitAll)
                authorize(anyRequest, authenticated)
            }
            oauth2ResourceServer {
                jwt {
                    jwtAuthenticationConverter = azureJwtAuthenticationConverter
                }
            }
            csrf { disable() }
        }
        return http.build()
    }

    open fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        configuration.allowedOrigins =
            listOf(
                "https://familie-ks-infotrygd.intern.dev.nav.no",
                "https://familie-ks-infotrygd.intern.nav.no",
                "http://localhost:8080",
            )
        configuration.allowedMethods = listOf("GET", "POST", "OPTIONS")
        configuration.allowedHeaders =
            listOf(
                "Content-Type",
                "Accept",
                "Authorization",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
            )
        configuration.allowCredentials = true
        configuration.maxAge = 3600L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
