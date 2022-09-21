package no.nav.infotrygd.kontantstotte.model.converters

import no.nav.commons.foedselsnummer.Foedselsnummer
import javax.persistence.AttributeConverter

class ReversedLongFoedselNrConverter : AttributeConverter<Foedselsnummer?, Long?> {
    private val converter = ReversedFoedselNrConverter()

    override fun convertToDatabaseColumn(attribute: Foedselsnummer?): Long? {
        return converter.convertToDatabaseColumn(attribute)?.toLong() ?: 0
    }

    override fun convertToEntityAttribute(dbData: Long?): Foedselsnummer? {
        return converter.convertToEntityAttribute(dbData?.toString()?.padStart(11, '0'))
    }
}