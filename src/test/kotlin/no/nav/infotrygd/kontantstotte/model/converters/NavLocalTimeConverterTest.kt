package no.nav.infotrygd.kontantstotte.model.converters

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalTime

internal class NavLocalTimeConverterTest {
    private val converter = NavLocalTimeConverter()

    @Test
    fun nullverdier() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull()
        assertThat(converter.convertToEntityAttribute(null)).isNull()
    }

    @Test
    fun midnatt() {
        assertThat(converter.convertToDatabaseColumn(LocalTime.MIDNIGHT)).isEqualTo("000000")
        assertThat(converter.convertToEntityAttribute("000000")).isEqualTo(LocalTime.MIDNIGHT)
    }

    @Test
    fun `lave verdier`() {
        assertThat(converter.convertToDatabaseColumn(LocalTime.of(0, 5, 6))).isEqualTo("000506")
        assertThat(converter.convertToEntityAttribute("000506")).isEqualTo(LocalTime.of(0, 5, 6))
    }

    @Test
    fun `høye verdier`() {
        assertThat(converter.convertToDatabaseColumn(LocalTime.of(13, 46, 57))).isEqualTo("134657")
        assertThat(converter.convertToEntityAttribute("134657")).isEqualTo(LocalTime.of(13, 46, 57))
    }
}