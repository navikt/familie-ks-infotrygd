package no.nav.infotrygd.kontantstotte.utils

import java.time.YearMonth
import java.time.format.DateTimeParseException

enum class DatoFormat {
    MMYYYY,
    YYYYMM
}

fun fromSeqToYearMonthOrNull(seq: String?, format: DatoFormat): YearMonth? {
    if (seq.isNullOrBlank()) {
        return null
    }

    val nullverdi = 999999L

    return try {
        val s = seq.toLong()
        val normalform = (nullverdi - s).toString().padStart(6, '0')
        when (format) {
            DatoFormat.MMYYYY -> parseMMYYYY(normalform)
            DatoFormat.YYYYMM -> parseYYYYMM(normalform)
        }
    } catch (e: Exception) {
        null
    }
}


fun parseYYYYMM(value: String): YearMonth {
    val (aarStr, maanedStr) = try {
        """^(\d\d\d\d)(\d\d)$""".toRegex().find(value)!!.destructured
    } catch (e: Exception) {
        throw DateTimeParseException("Kunne ikke parse dato. Forventet format MMYYYY.", value, 0)
    }

    return YearMonth.of(aarStr.toInt(), maanedStr.toInt())
}

fun parseMMYYYY(value: String): YearMonth {
    val (maanedStr, aarStr) = try {
        """^(\d\d)(\d\d\d\d)$""".toRegex().find(value)!!.destructured
    } catch (e: Exception) {
        throw DateTimeParseException("Kunne ikke parse dato. Forventet format MMYYYY.", value, 0)
    }

    return YearMonth.of(aarStr.toInt(), maanedStr.toInt())
}