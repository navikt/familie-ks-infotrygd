package no.nav.infotrygd.kontantstotte.testutil

import no.nav.commons.foedselsnummer.Foedselsnummer
import no.nav.commons.foedselsnummer.Kjoenn
import no.nav.commons.foedselsnummer.testutils.FoedselsnummerGenerator
import java.time.LocalDate

object TestData {
    fun foedselsNr(
        foedselsdato: LocalDate? = null,
        kjoenn: Kjoenn = Kjoenn.MANN): Foedselsnummer {

        return fnrGenerator.foedselsnummer(
            foedselsdato = foedselsdato,
            kjoenn = kjoenn
        )
    }

    private val fnrGenerator = FoedselsnummerGenerator()
}