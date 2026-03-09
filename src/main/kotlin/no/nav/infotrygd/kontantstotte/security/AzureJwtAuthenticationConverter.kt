package no.nav.infotrygd.kontantstotte.security

import no.nav.infotrygd.kontantstotte.security.Rolle
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component

const val ACCESS_AS_APPLICATION_ROLE = "access_as_application"

@Component
class AzureJwtAuthenticationConverter(
    @param:Value("\${TEAMFAMILIE_FORVALTNING_GROUP_ID}") private val forvalterGroupId: String,
) : Converter<Jwt, AbstractAuthenticationToken> {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val secureLogger: Logger = LoggerFactory.getLogger("secureLogger")

    companion object {
        private const val GROUPS_CLAIM = "groups"
        private const val ROLES_CLAIM = "roles"
    }

    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val grupper = jwt.getClaimAsStringList(GROUPS_CLAIM) ?: emptyList()
        val roles = jwt.getClaimAsStringList(ROLES_CLAIM) ?: emptyList()

        val roller =
            buildSet {
                if (grupper.contains(forvalterGroupId)) add(Rolle.FORVALTER)
                if (roles.contains(ACCESS_AS_APPLICATION_ROLE)) add(Rolle.APPLICATION)
            }

        if (roller.isEmpty()) {
            secureLogger.warn(
                "Bruker har ingen gyldige roller. Grupper i token: ${grupper.joinToString(", ")}",
            )
        }

        val authorities = roller.map { SimpleGrantedAuthority(it.authority()) }

        return JwtAuthenticationToken(jwt, authorities)
    }
}
