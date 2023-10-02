package no.nav.infotrygd.kontantstotte.testutil

import no.nav.infotrygd.kontantstotte.model.ks.Barn
import no.nav.infotrygd.kontantstotte.model.ks.Stonad
import no.nav.infotrygd.kontantstotte.model.ks.Utbetaling
import no.nav.infotrygd.kontantstotte.nextId
import java.math.BigDecimal
import java.time.YearMonth

class StonadFactory(
    val region: String = "",
    val personkey: BigDecimal = nextId().toBigDecimal(),
    val iverfomSeq: String = nextId().toString(),
    val virkfomSeq: String = nextId().toString()
) {
    fun stonad(
        barnEksempler: List<Barn> = emptyList(),
        utbetalingerEksempler: List<Utbetaling> = emptyList(),
        opphoertVfom: String? = null
    ): Stonad {
        val barn = barnEksempler.map { it.copy(
            region = region,
            personkey = personkey,
            iverSeq = iverfomSeq,
            virkfomSeq = virkfomSeq
        ) }

        val utbetalinger = utbetalingerEksempler.map { it.copy(
            region = region,
            personkey = personkey,
            startUtbetaltMndSeq = iverfomSeq,
            virkfomSeq = virkfomSeq
        ) }

        return Stonad(
            id = nextId().toBigDecimal(),
            region = region,
            personkey = personkey,
            iverfomSeq = iverfomSeq,
            virkfomSeq = virkfomSeq,
            opphoertVfom = opphoertVfom,
            fnr = TestData.foedselsNr(),
            barn = barn,
            utbetalinger = utbetalinger
        )
    }

    fun barn(): Barn {
        return Barn(
            region = region,
            personkey = personkey,
            iverSeq = iverfomSeq,
            virkfomSeq = virkfomSeq,

            id = nextId().toBigDecimal(),

            fnr = TestData.foedselsNr()
        )
    }

    fun utbetaling(): Utbetaling {
        return Utbetaling(
            region = region,
            personkey = personkey,
            startUtbetaltMndSeq = iverfomSeq,
            virkfomSeq = virkfomSeq,

            id = nextId().toBigDecimal(),

            fom = YearMonth.of(2020, 1),
            tom = YearMonth.of(2020, 1),
            belop = 0,
            type = ""
        )
    }
}