package no.nav.infotrygd.kontantstotte.service

import no.nav.commons.foedselsnummer.Foedselsnummer
import no.nav.infotrygd.kontantstotte.dto.BarnDto
import no.nav.infotrygd.kontantstotte.dto.InnsynRequest
import no.nav.infotrygd.kontantstotte.dto.InnsynResponse
import no.nav.infotrygd.kontantstotte.dto.StonadDto
import no.nav.infotrygd.kontantstotte.dto.tilFoedselsnummere
import no.nav.infotrygd.kontantstotte.dto.tilReversert
import no.nav.infotrygd.kontantstotte.model.ks.Stonad
import no.nav.infotrygd.kontantstotte.repository.BarnRepository
import no.nav.infotrygd.kontantstotte.repository.StonadRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.YearMonth

@Service
class InnsynService(
    private val stonadRepository: StonadRepository,
    private val barnRepository: BarnRepository
) {
    fun hentDataForSøker(req: InnsynRequest): InnsynResponse {
        val stonader = stonadRepository.findByFnrIn(req.barn.map { Foedselsnummer(it) })
        return InnsynResponse(
            data = stonader.map { it.toDto() }
        )
    }

    fun hentDataForBarn(req: InnsynRequest): InnsynResponse {
        val barna = barnRepository.findByFnrIn(req.barn.tilFoedselsnummere())
        val stonader = stonadRepository.findByBarnIn(barna)
        return InnsynResponse(
            data = stonader.map { it.toDto() }
        )
    }

    fun harKontantstotte(req: InnsynRequest): Boolean {
        val barna = barnRepository.findByFnrIn(req.barn.tilFoedselsnummere())
        val stonader = stonadRepository.findByBarnIn(barna).filter { it.tom == null || it.tom!! > YearMonth.now() }

        logger.info("Fant ${stonader.size} for barna")
        secureLogger.info("Fant ${stonader.size} for barna $barna")
        return stonader.isNotEmpty()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(InnsynService::class.java)
        private val secureLogger: Logger = LoggerFactory.getLogger("secureLogger")
    }
}

private fun Stonad.toDto(): StonadDto {
    return StonadDto(
        fnr = this.fnr,
        fom = this.fom,
        tom = this.tom,
        belop = this.belop,
        barn = this.barn.map { BarnDto(fnr = Foedselsnummer.tilReversert(it.fnr.asString)) }
    )
}
