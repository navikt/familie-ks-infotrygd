package no.nav.infotrygd.kontantstotte.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class TilgangskontrollService(
    @Value("\${TEAMFAMILIE_SAKSBEHANDLER_GROUP_ID}") private val saksbehandlerGroupId: String,
    @Value("\${TEAMFAMILIE_FORVALTNING_GROUP_ID}") private val forvalterGroupId: String,
) {
    val secureLogger: Logger = LoggerFactory.getLogger("secureLogger")

    fun sjekkTilgang() {
        val jwt = (SecurityContextHolder.getContext().authentication as? JwtAuthenticationToken)?.token
        val roles = jwt?.getClaimAsStringList("roles") ?: emptyList()
        val groups = jwt?.getClaimAsStringList("groups") ?: emptyList()

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
