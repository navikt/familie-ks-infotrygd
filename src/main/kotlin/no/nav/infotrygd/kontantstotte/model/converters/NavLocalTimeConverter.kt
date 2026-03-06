package no.nav.infotrygd.kontantstotte.model.converters

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class NavLocalTimeConverter : AttributeConverter<LocalTime?, String?> {
    private val formatter = DateTimeFormatter.ofPattern("HHmmss");

    override fun convertToDatabaseColumn(attribute: LocalTime?): String? {
        return attribute?.let { formatter.format(attribute) }
    }

    override fun convertToEntityAttribute(dbData: String?): LocalTime? {
        return dbData?.let { LocalTime.parse(dbData, formatter) }
    }
}