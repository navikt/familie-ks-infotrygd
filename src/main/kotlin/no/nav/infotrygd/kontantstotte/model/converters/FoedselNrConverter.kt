package no.nav.infotrygd.kontantstotte.model.converters

import no.nav.commons.foedselsnummer.Foedselsnummer
import jakarta.persistence.AttributeConverter

class FoedselNrConverter  : AttributeConverter<Foedselsnummer?, String?> {
    override fun convertToDatabaseColumn(attribute: Foedselsnummer?): String? {
        return attribute?.asString
    }

    override fun convertToEntityAttribute(dbData: String?): Foedselsnummer? {
        return dbData?.let { Foedselsnummer(it) }
    }
}