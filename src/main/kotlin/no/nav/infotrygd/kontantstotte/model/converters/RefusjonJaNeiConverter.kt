package no.nav.infotrygd.kontantstotte.model.converters

import jakarta.persistence.AttributeConverter

class RefusjonJaNeiConverter : AttributeConverter<Boolean, String?> {
    private object CharConverter : AbstractCharConverter(1)

    override fun convertToDatabaseColumn(attribute: Boolean?): String? {
        if (attribute == true) {
            return CharConverter.convertToDatabaseColumn("J")
        }
        return CharConverter.convertToDatabaseColumn(null)
    }

    override fun convertToEntityAttribute(dbData: String?): Boolean = CharConverter.convertToEntityAttribute(dbData) != null
}
