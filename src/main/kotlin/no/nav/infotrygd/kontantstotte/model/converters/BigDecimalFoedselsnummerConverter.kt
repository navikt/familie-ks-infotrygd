package no.nav.infotrygd.kontantstotte.model.converters

import no.nav.commons.foedselsnummer.Foedselsnummer
import java.math.BigDecimal
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class BigDecimalFoedselsnummerConverter : AttributeConverter<Foedselsnummer?, BigDecimal?> {
    override fun convertToDatabaseColumn(attribute: Foedselsnummer?): BigDecimal? {
        val fnr = attribute
            ?: return null

        return fnr.asString.toBigDecimal()
    }

    override fun convertToEntityAttribute(dbData: BigDecimal?): Foedselsnummer? {
        val fnr = dbData
            ?: return null

        if (dbData == BigDecimal.ZERO) {
            return null
        }

        val str = fnr.toString().padStart(11, '0')
        return Foedselsnummer(str)
    }
}