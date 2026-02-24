package no.nav.infotrygd.kontantstotte.config

enum class Rolle(
    val beskrivelse: String,
) {
    FORVALTER("Utvikler"),
    ACCESS_AS_APPLICATION("Tilgang til applikasjonen"),
    ;

    fun authority(): String = "ROLE_$name"

    companion object {
        val AZURE_GRUPPE_TIL_ROLLE: Map<String, Set<Rolle>> =
            mapOf(
                "c62e908a-cf20-4ad0-b7b3-3ff6ca4bf38b" to setOf(FORVALTER),
            )

        fun fraAzureGrupper(gruppeIder: List<String>): Set<Rolle> =
            gruppeIder
                .flatMap { gruppeId -> AZURE_GRUPPE_TIL_ROLLE[gruppeId] ?: emptySet() }
                .toSet()
    }
}
