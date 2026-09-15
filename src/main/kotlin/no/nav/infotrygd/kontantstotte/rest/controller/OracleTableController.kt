package no.nav.infotrygd.kontantstotte.rest.controller

import no.nav.infotrygd.kontantstotte.integration.OracleTable
import no.nav.infotrygd.kontantstotte.integration.OracleTableIntegration
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@PreAuthorize("hasRole('FORVALTER') or hasRole('APPLICATION')")
@RestController
@RequestMapping("/api")
class OracleTableController(
    private val oracleTableIntegration: OracleTableIntegration,
) {
    @GetMapping("/database-tables")
    @Transactional(readOnly = true)
    fun get(): List<OracleTable> = oracleTableIntegration.getTables()
}
