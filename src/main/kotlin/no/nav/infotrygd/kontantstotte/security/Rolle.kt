package no.nav.infotrygd.kontantstotte.security

enum class Rolle(
    val beskrivelse: String,
) {
    FORVALTER("Forvalter"),
    APPLICATION("Tilgang til applikasjonen"),
    ;

    fun authority(): String = "ROLE_$name"
}
