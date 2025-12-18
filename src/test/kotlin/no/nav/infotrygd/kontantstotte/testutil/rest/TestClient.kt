package no.nav.infotrygd.kontantstotte.testutil.rest

import no.nav.infotrygd.kontantstotte.dto.InnsynRequest
import no.nav.infotrygd.kontantstotte.dto.InnsynResponse
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.client.postForObject

class TestClient(
    private val restTemplate: RestTemplate,
) {
    fun health() {
        restTemplate.getForObject<String>("/actuator/health")
    }

    fun hentPerioder(req: InnsynRequest): InnsynResponse = restTemplate.postForObject("/api/hentPerioderMedKontantstøtteIInfotrygd", req)!!

    fun hentAlleBarnMedLøpendeFagsak(): List<String> = restTemplate.getForObject("/api/hentidentertilbarnmedlopendesaker")!!
}
