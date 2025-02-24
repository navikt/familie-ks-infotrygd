package no.nav.infotrygd.kontantstotte.config

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@EnableJwtTokenValidation(ignore = ["org.springframework", "org.springdoc"])
@EnableConfigurationProperties(DatasourceConfiguration::class)
@Configuration
class SecurityConfiguration
