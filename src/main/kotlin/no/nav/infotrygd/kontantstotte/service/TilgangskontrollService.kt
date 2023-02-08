package no.nav.infotrygd.kontantstotte.service

import no.nav.security.token.support.core.context.TokenValidationContextHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class TilgangskontrollService(
    private val tokenValidationContextHolder: TokenValidationContextHolder,
    @Value("\${TEAMFAMILIE_SAKSBEHANDLER_GROUP_ID}") private val saksbehandlerGroupId: String,
    @Value("\${TEAMFAMILIE_FORVALTNING_GROUP_ID}") private val forvalterGroupId: String
) {

    val secureLogger: Logger = LoggerFactory.getLogger("secureLogger")

    fun sjekkTilgang() {
        val roles = tokenValidationContextHolder.tokenValidationContext.getClaims("azuread").map {
            it.getAsList("roles")
        }.orElse(emptyList())
        val groups = tokenValidationContextHolder.tokenValidationContext.getClaims("azuread").map {
            it.getAsList("groups")
        }.orElse(emptyList())

        secureLogger.info("Roller: $roles")
        secureLogger.info("Grupper: $groups")
        if (!(roles.contains(ACCESS_AS_APPLICATION_ROLE) || groups.contains(saksbehandlerGroupId) || groups.contains(forvalterGroupId))) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "User har ikke tilgang til å kalle tjenesten!")
        }
    }

    companion object {
        const val ACCESS_AS_APPLICATION_ROLE = "access_as_application"
    }
}
