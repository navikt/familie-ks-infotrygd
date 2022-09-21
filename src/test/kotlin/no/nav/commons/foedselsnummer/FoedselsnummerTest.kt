package no.nav.commons.foedselsnummer

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class FoedselsnummerTest {
    @Test
    fun kjoenn() {
        val mann = Foedselsnummer("00000000191")
        val kvinne = Foedselsnummer("00000000272")

        assertThat(mann.kjoenn).isEqualTo(Kjoenn.MANN)
        assertThat(kvinne.kjoenn).isEqualTo(Kjoenn.KVINNE)
    }
}