package no.nav.infotrygd.kontantstotte.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.YearMonth

internal class ConvertSeqDatoKtTest {
    private val max = 999999L

    @Test
    internal fun mmyyyy() {
        val datoSeq = max - 121999
        val dato = fromSeqToYearMonthOrNull(datoSeq.toString(), DatoFormat.MMYYYY)
        assertThat(dato).isEqualTo(YearMonth.of(1999, 12))
    }

    @Test
    internal fun yyyymm() {
        val datoSeq = max - 199912
        val dato = fromSeqToYearMonthOrNull(datoSeq.toString(), DatoFormat.YYYYMM)
        assertThat(dato).isEqualTo(YearMonth.of(1999, 12))
    }

    @Test
    internal fun nullverdier() {
        assertThat(fromSeqToYearMonthOrNull("", DatoFormat.YYYYMM)).isNull()
        assertThat(fromSeqToYearMonthOrNull(null, DatoFormat.YYYYMM)).isNull()
        assertThat(fromSeqToYearMonthOrNull("tullball", DatoFormat.YYYYMM)).isNull()
    }
}