package no.nav.infotrygd.kontantstotte.model.converters

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.YearMonth

internal class NavCharYearMonthConverterTest {

    private val converter = NavCharYearMonthConverter()

    @Test
    fun convertToDatabaseColumn() {
        assertThat(converter.convertToDatabaseColumn(YearMonth.of(2020, 11))).isEqualTo("112020")
        assertThat(converter.convertToDatabaseColumn(null)).isNull()
    }

    @Test
    fun convertToEntityAttribute() {
        assertThat(converter.convertToEntityAttribute("112020")).isEqualTo(YearMonth.of(2020, 11))
        assertThat(converter.convertToEntityAttribute("12020")).isEqualTo(YearMonth.of(2020, 1))
        assertThat(converter.convertToEntityAttribute(null)).isNull()

        assertThat(converter.convertToEntityAttribute("000000")).isNull()
        assertThat(converter.convertToEntityAttribute("999999")).isNull()
    }
}