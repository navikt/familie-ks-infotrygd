package no.nav.infotrygd.kontantstotte.security

enum class Rolle {
    FORVALTER, // "Bruker har token med en gruppe som innholder forvalterrolle"
    APPLICATION, // "Maskin til maskin token med rolle access_as_application"
    ;

    fun authority(): String = "ROLE_$name"
}
