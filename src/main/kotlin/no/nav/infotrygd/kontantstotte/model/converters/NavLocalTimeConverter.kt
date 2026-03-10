package no.nav.infotrygd.kontantstotte.model.converters

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Converter
class NavLocalTimeConverter : AttributeConverter<LocalTime?, String?> {
    private val formatter = DateTimeFormatter.ofPattern("HHmmss")

    override fun convertToDatabaseColumn(attribute: LocalTime?): String? = attribute?.let { formatter.format(attribute) }

    override fun convertToEntityAttribute(dbData: String?): LocalTime? = dbData?.let { LocalTime.parse(dbData, formatter) }
}
