package no.nav.infotrygd.kontantstotte.repository

import jakarta.persistence.EntityManager
import no.nav.infotrygd.kontantstotte.testutil.AbstractStonadFactoryTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
@ActiveProfiles("test")
class StonadRepositoryTest : AbstractStonadFactoryTest() {
    @Autowired
    private lateinit var stonadRepository: StonadRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Test
    @Order(2)
    fun `alle relasjoner er tilstede`() {
        val barn = sf.barn()
        val utbetaling = sf.utbetaling()
        val stonad =
            sf.stonad(
                barnEksempler = listOf(barn),
                utbetalingerEksempler = listOf(utbetaling),
                opphoertVfom = "000000",
            )

        stonadRepository.save(stonad)

        entityManager.flush()
        entityManager.clear()

        val res = stonadRepository.findByFnrIn(listOf(stonad.fnr))
        assertThat(res).hasSameSizeAs(listOf(stonad))

        val resStonad = res[0]
        assertThat(resStonad.id).isEqualTo(stonad.id)

        assertThat(resStonad.barn).hasSameSizeAs(listOf(barn))
        assertThat(resStonad.barn[0].id).isEqualTo(barn.id)

        assertThat(resStonad.utbetalinger).hasSameSizeAs(listOf(utbetaling))
        assertThat(resStonad.utbetalinger[0].id).isEqualTo(utbetaling.id)
    }

    @Test
    @Order(1)
    fun `test hent alle barn med løpende fagsak`() {
        val barn = sf.barn()
        val utbetaling = sf.utbetaling()
        val stonad =
            sf.stonad(
                barnEksempler = listOf(barn),
                utbetalingerEksempler = listOf(utbetaling),
                opphoertVfom = "000000",
            )

        stonadRepository.save(stonad)

        entityManager.flush()
        entityManager.clear()

        val res = stonadRepository.findByOpphoertVfomEquals("000000")
        assertThat(res).hasSameSizeAs(listOf(stonad))

        val resStonad = res[0]
        assertThat(resStonad.id).isEqualTo(stonad.id)

        assertThat(resStonad.barn).hasSameSizeAs(listOf(barn))
        assertThat(resStonad.barn[0].id).isEqualTo(barn.id)

        assertThat(resStonad.utbetalinger).hasSameSizeAs(listOf(utbetaling))
        assertThat(resStonad.utbetalinger[0].id).isEqualTo(utbetaling.id)
    }
}
