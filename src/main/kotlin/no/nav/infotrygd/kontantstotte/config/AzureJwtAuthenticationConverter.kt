package no.nav.infotrygd.kontantstotte.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component

@Component
class AzureJwtAuthenticationConverter(
    @param:Value("\${TEAMFAMILIE_FORVALTNING_GROUP_ID}") private val forvalterGroupId: String,
) : Converter<Jwt, AbstractAuthenticationToken> {
    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val GROUPS_CLAIM = "groups"
        private const val ROLES_CLAIM = "roles"
        private const val ACCESS_AS_APPLICATION_ROLE = "access_as_application"
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
            logger.warn(
                "Bruker har ingen gyldige roller. Grupper i token: ${grupper.joinToString(", ")}",
            )
            throw UtilstrekkeligTilgangException(
                "Bruker har ikke tilgang til applikasjonen. Mangler påkrevde gruppemedlemskap.",
            )
        }

        val authorities = roller.map { SimpleGrantedAuthority(it.authority()) }

        logger.info("Autentisert bruker med roller: ${roller.joinToString(", ")}")

        return JwtAuthenticationToken(jwt, authorities)
    }
}

class ManglendeClaimException(
    message: String,
) : RuntimeException(message)

class UtilstrekkeligTilgangException(
    message: String,
) : RuntimeException(message)
