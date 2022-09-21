package no.nav.infotrygd.kontantstotte.repository

import no.nav.commons.foedselsnummer.Foedselsnummer
import no.nav.infotrygd.kontantstotte.model.ks.Barn
import no.nav.infotrygd.kontantstotte.model.ks.Stonad
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
interface StonadRepository : JpaRepository<Stonad, BigDecimal> {

    fun findByFnrIn(fnr: List<Foedselsnummer>): List<Stonad>

    fun findByBarnIn(barn: List<Barn>) : List<Stonad>
}