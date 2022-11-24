package no.nav.infotrygd.kontantstotte.rest.controller

import io.micrometer.core.annotation.Timed
import no.nav.infotrygd.kontantstotte.dto.InnsynRequest
import no.nav.infotrygd.kontantstotte.dto.InnsynResponse
import no.nav.infotrygd.kontantstotte.service.InnsynService
import no.nav.infotrygd.kontantstotte.service.TilgangskontrollService
import no.nav.security.token.support.core.api.Protected
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Protected
@RestController
@RequestMapping("/api")
@Timed(value = "infotrygd_kontantstottev2_innsyn_controller", percentiles = [0.5, 0.95])
class InnsynController(
    private val innsynService: InnsynService,
    private val tilgangskontrollService: TilgangskontrollService
) {

    @PostMapping("/hentPerioder")
    fun hentPerioder(@RequestBody req: InnsynRequest): InnsynResponse {
        tilgangskontrollService.sjekkTilgang()
        return innsynService.hentData(req)
    }

    @PostMapping("/harKontantstotte")
    fun harKontantstotteIInfotrygd(@RequestBody req: InnsynRequest): Boolean {
        tilgangskontrollService.sjekkTilgang()
        return innsynService.harKontantstotte(req)
    }
}
