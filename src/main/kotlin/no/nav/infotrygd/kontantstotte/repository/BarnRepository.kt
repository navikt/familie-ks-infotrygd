package no.nav.infotrygd.kontantstotte.repository

import no.nav.commons.foedselsnummer.Foedselsnummer
import no.nav.infotrygd.kontantstotte.model.ks.Barn
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
interface BarnRepository : JpaRepository<Barn, BigDecimal> {
    fun findByFnrIn(fnr: List<Foedselsnummer>): List<Barn>

}