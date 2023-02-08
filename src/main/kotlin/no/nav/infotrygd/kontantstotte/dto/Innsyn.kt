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
    return this.map { Foedselsnummer.tilReversert(it) }
}

fun Foedselsnummer.Companion.tilReversert(reversert: String): Foedselsnummer {
    return Foedselsnummer(reverse(reversert))
}

private val regex = """(\d\d)(\d\d)(\d\d)(\d{5})""".toRegex()

private fun reverse(fnr: String): String {
    require(regex.matches(fnr)) { "Ikke et gyldig (reversert?) fødselsnummer: $fnr" }

    val (a, b, c, pnr) = regex.find(fnr)!!.destructured
    return "$c$b$a$pnr"
}
