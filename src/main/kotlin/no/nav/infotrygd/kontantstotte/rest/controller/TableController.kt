package no.nav.infotrygd.kontantstotte.rest.controller

import jakarta.transaction.Transactional
import no.nav.infotrygd.kontantstotte.integration.TableIntegrator
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Transactional
class TableController(
    private val tableIntegrator: TableIntegrator,
) {
    @GetMapping(path = ["/tables"])
    fun get(): Map<String, List<String>> = tableIntegrator.tables
}
