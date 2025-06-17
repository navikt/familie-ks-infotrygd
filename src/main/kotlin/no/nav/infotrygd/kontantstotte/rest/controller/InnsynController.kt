package no.nav.infotrygd.kontantstotte.rest.controller

import io.micrometer.core.annotation.Timed
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.trace.Span
import no.nav.infotrygd.kontantstotte.dto.InnsynRequest
import no.nav.infotrygd.kontantstotte.dto.InnsynResponse
import no.nav.infotrygd.kontantstotte.service.InnsynService
import no.nav.infotrygd.kontantstotte.service.SøkerOgBarn
import no.nav.infotrygd.kontantstotte.service.TilgangskontrollService
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@ProtectedWithClaims(issuer = "azuread")
@RestController
@RequestMapping("/api")
@Timed(value = "infotrygd_kontantstottev2_innsyn_controller", percentiles = [0.5, 0.95])
class InnsynController(
    private val innsynService: InnsynService,
    private val tilgangskontrollService: TilgangskontrollService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/hentPerioderMedKontantstøtteIInfotrygd", "/hentPerioderMedKontantstotteIInfotrygd")
    fun hentPerioder(
        @RequestBody req: InnsynRequest,
    ): InnsynResponse {
        tilgangskontrollService.sjekkTilgang()
        return innsynService.hentDataForSøker(req)
    }

    @PostMapping("/hentPerioderMedKontantstøtteIInfotrygdByBarn", "/hentPerioderMedKontantstotteIInfotrygdByBarn")
    fun hentPerioderForBarn(
        @RequestBody req: InnsynRequest,
    ): InnsynResponse {
        tilgangskontrollService.sjekkTilgang()
        return innsynService.hentDataForBarn(req)
    }

    @GetMapping("/hentidentertilbarnmedlopendesaker")
    fun harKontantstotteIInfotrygd(): List<String> {
        tilgangskontrollService.sjekkTilgang()
        return innsynService.hentbarnmedløpendekontantstøtte()
    }

    @GetMapping("/hent-soekere-og-barn-med-loepende-kontantstoette")
    fun hentSøkereOgBarnMedLøpendeKontantstøtteIInfotrygd(): List<SøkerOgBarn> {
        tilgangskontrollService.sjekkTilgang()
        return innsynService.hentSøkerOgBarnMedLøpendeKontantstøtte()
    }

    @GetMapping("/uuid/{uuid}")
    fun handleUuid(
        @PathVariable uuid: UUID,
    ): String = getCurrentTraceId()

    fun getCurrentTraceId(): String {
        val currentSpan: Span = Span.current()
        val spanContext = currentSpan.spanContext

        kjørIEgenTrace {
            innsynService.hentSøkerOgBarnMedLøpendeKontantstøtte()
        }

        return if (spanContext.isValid) {
            spanContext.traceId
        } else {
            "No valid trace ID, guess you're debugging in the void"
        }
    }

    fun <T> kjørIEgenTrace(body: () -> T): T {
        val tracer = GlobalOpenTelemetry.getTracer("task")

        val functionName = body::class.java.enclosingMethod?.name ?: "Unknown function"
        val newRootSpan = tracer.spanBuilder(functionName).setNoParent().startSpan()
        newRootSpan.makeCurrent()
        val newTraceId = newRootSpan.spanContext.traceId
        logger.info("TraceId: $newTraceId")

        return try {
            body()
        } finally {
            newRootSpan.end()
        }
    }
}
