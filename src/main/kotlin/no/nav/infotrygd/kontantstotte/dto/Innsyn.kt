package no.nav.infotrygd.kontantstotte.dto

import no.nav.commons.foedselsnummer.Foedselsnummer
import java.time.YearMonth

data class InnsynRequest(
    val barn: List<String>
)

data class InnsynResponse(
    val data: List<StonadDto>
)

data class StonadDto(
    val fnr: Foedselsnummer,
    val fom: YearMonth?,
    val tom: YearMonth?,
    val belop: Int?,
    val barn: List<BarnDto>
)

data class BarnDto(
    val fnr: Foedselsnummer
)

fun List<String>.tilFoedselsnummere(): List<Foedselsnummer> {
    return this.map { Foedselsnummer(it) }
}
