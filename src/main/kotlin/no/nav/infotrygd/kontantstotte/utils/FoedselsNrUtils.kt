package no.nav.infotrygd.kontantstotte.utils

import no.nav.commons.foedselsnummer.Foedselsnummer


val Foedselsnummer.reversert: String
    get() {
        return reverse(asString)
    }

fun Foedselsnummer.Companion.fraReversert(reversert: String): Foedselsnummer {
    return Foedselsnummer(reverse(reversert))
}

private val regex = """(\d\d)(\d\d)(\d\d)(\d{5})""".toRegex()

private fun reverse(fnr: String): String {
    require(regex.matches(fnr)) { "Ikke et gyldig (reversert?) fødselsnummer: $fnr" }

    val (a, b, c, pnr) = regex.find(fnr)!!.destructured
    return "$c$b$a$pnr"
}