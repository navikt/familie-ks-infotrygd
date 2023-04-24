package no.nav.infotrygd.kontantstotte.model.converters

import org.slf4j.LoggerFactory
import java.time.YearMonth
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class NavCharYearMonthConverter : AttributeConverter<YearMonth?, String?> {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun convertToDatabaseColumn(attribute: YearMonth?): String? {
        if (attribute == null) {
            return null
        }

        return "${attribute.monthValue}${attribute.year}"
    }

    override fun convertToEntityAttribute(dbData: String?): YearMonth? {
        if (dbData == null) {
            return null
        }
        val (maanedStr, aarStr) = try {
            """^(\d\d)(\d\d\d\d)$""".toRegex().find("%06d".format(dbData.toInt()))!!.destructured
        } catch (e: Exception) {
            logger.warn("Ukjent databaseverdi. Forventet år/måned på format MMYYYY, fikk: \"$dbData\"")
            return null
        }

        val (maaned, aar) = maanedStr.toInt() to aarStr.toInt()
        try {
            return YearMonth.of(aar, maaned)
        } catch (e: Exception) {
            logger.warn("Ukjent databaseverdi. Forventet år/måned på format MMYYYY, fikk: \"$dbData\"")
            return null
        }
    }
}