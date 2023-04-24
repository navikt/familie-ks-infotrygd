package no.nav.infotrygd.kontantstotte.testutil

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import jakarta.annotation.PostConstruct

@Component
class DebugComponent(
    @Value("\${mock-oauth2-server.port}") val oauthPort: String
) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun x() {
        logger.info("""
            
            ================================================================================
            DEBUG INFO
            ================================================================================
            Mock OAuth2 server port: $oauthPort
            ================================================================================
        """.trimIndent())
    }
}