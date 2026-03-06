package no.nav.infotrygd.kontantstotte.exception

class UkjentDatabaseverdiException(
    val verdi: String,
    gyldigeVerdier: List<String>,
) : RuntimeException("Ukjent databaseverdi '$verdi'. Tillatte verdier er: ${gyldigeVerdier.joinToString()}")
