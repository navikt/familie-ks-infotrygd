package no.nav.infotrygd.kontantstotte.service

import no.nav.commons.foedselsnummer.Foedselsnummer
import no.nav.infotrygd.kontantstotte.dto.*
import no.nav.infotrygd.kontantstotte.model.ks.Stonad
import no.nav.infotrygd.kontantstotte.repository.BarnRepository
import no.nav.infotrygd.kontantstotte.repository.StonadRepository
import org.springframework.stereotype.Service

@Service
class InnsynService(
    private val stonadRepository: StonadRepository,
    private val barnRepository: BarnRepository
) {
    fun hentData(req: InnsynRequest): InnsynResponse {
        val stonader = stonadRepository.findByFnrIn(req.barn.tilFoedselsnummere())
        return InnsynResponse(
            data = stonader.map { it.toDto() }
        )
    }

    fun harKontantstotte(req: InnsynRequest): Boolean {
        val barna = barnRepository.findByFnrIn(req.barn.tilFoedselsnummere())
        val stonader = stonadRepository.findByBarnIn(barna)

        return stonader.isNotEmpty()
    }
}

private fun Stonad.toDto(): StonadDto {
    return StonadDto(
        fnr = this.fnr,
        fom = this.fom,
        tom = this.tom,
        belop = this.belop,
        barn = this.barn.map {
            BarnDto(
                fnr = it.fnr
            )
        }
    )
}
