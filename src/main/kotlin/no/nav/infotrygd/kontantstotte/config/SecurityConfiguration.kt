package no.nav.infotrygd.kontantstotte.config

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.context.annotation.Configuration

@EnableJwtTokenValidation(ignore = ["org.springframework", "org.springdoc"])
@Configuration
class SecurityConfiguration
