package no.nav.infotrygd.kontantstotte.model.converters

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

abstract class AbstractCharConverter(private val size: Int) : AttributeConverter<String?, String?> {
    override fun convertToDatabaseColumn(attribute: String?): String? {
        val str = attribute ?: ""
        return str.padEnd(size, ' ')
    }

    override fun convertToEntityAttribute(dbData: String?): String? {
        if(dbData.isNullOrBlank()) {
            return null
        }

        return dbData.trimEnd()
    }

}

@Converter
class BrukerIdConverter : AbstractCharConverter(7)

@Converter
class Char2Converter : AbstractCharConverter(2)