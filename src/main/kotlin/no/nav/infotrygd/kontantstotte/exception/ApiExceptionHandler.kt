package no.nav.infotrygd.kontantstotte.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {
    private val logger = LoggerFactory.getLogger(ApiExceptionHandler::class.java)

    @ExceptionHandler(AuthenticationServiceException::class)
    fun handleAuthenticationServiceException(e: AuthenticationServiceException): ProblemDetail {
        val rootCause = generateSequence(e.cause) { it.cause }.lastOrNull() ?: e
        logger.error("Autentisering feilet: ${rootCause.message}")
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Autentisering feilet: ${rootCause.message}")
    }
}
