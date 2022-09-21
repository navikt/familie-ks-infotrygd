package no.nav.infotrygd.kontantstotte.exception

import java.lang.RuntimeException

class UkjentDatabaseverdiException(val verdi: String, gyldigeVerdier: List<String>)
    : RuntimeException("Ukjent databaseverdi '$verdi'. Tillatte verdier er: ${gyldigeVerdier.joinToString()}") {
}