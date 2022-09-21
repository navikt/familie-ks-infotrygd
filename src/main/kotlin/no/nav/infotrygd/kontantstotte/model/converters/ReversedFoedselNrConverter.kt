package no.nav.infotrygd.kontantstotte.model.converters

import no.nav.commons.foedselsnummer.Foedselsnummer
import no.nav.infotrygd.kontantstotte.utils.fraReversert
import no.nav.infotrygd.kontantstotte.utils.reversert
import javax.persistence.AttributeConverter

class ReversedFoedselNrConverter : AttributeConverter<Foedselsnummer?, String?> {
    override fun convertToDatabaseColumn(attribute: Foedselsnummer?): String? {
        return attribute?.reversert ?: "00000000000"
    }

    override fun convertToEntityAttribute(dbData: String?): Foedselsnummer? {
        if(dbData == null) {
            return null
        }

        if(dbData.toLong() == 0L) {
            return null
        }

        return Foedselsnummer.fraReversert(dbData)
    }
}