package no.nav.infotrygd.kontantstotte.model.converters

import no.nav.commons.foedselsnummer.Foedselsnummer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BigDecimalFoedselsnummerConverterTest {

    private val converter = BigDecimalFoedselsnummerConverter()

    @Test
    fun convertToDatabaseColumn() {
        assertThat(converter.convertToDatabaseColumn(Foedselsnummer("01234567890"))).isEqualTo(1234567890.toBigDecimal())
        assertThat(converter.convertToDatabaseColumn(null)).isNull()
    }

    @Test
    fun convertToEntityAttribute() {
        assertThat(converter.convertToEntityAttribute(1234567890.toBigDecimal())).isEqualTo(Foedselsnummer("01234567890"))
        assertThat(converter.convertToEntityAttribute(null)).isNull()
        assertThat(converter.convertToEntityAttribute(0.toBigDecimal())).isNull()
    }
}