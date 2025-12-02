package no.nav.infotrygd.kontantstotte.repository

import jakarta.persistence.EntityManager
import no.nav.infotrygd.kontantstotte.testutil.StonadFactory
import no.nav.infotrygd.kontantstotte.testutil.TestContainersConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
@ActiveProfiles("test")
@Import(TestContainersConfiguration::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StonadRepositoryTest {

    @Autowired
    private lateinit var stonadRepository: StonadRepository

    @Autowired
    private lateinit var entityManager: EntityManager

    @Test
    fun `alle relasjoner er tilstede`() {
        val sf = StonadFactory()
        val barn = sf.barn()
        val utbetaling = sf.utbetaling()
        val stonad = sf.stonad(
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
    fun `test hent alle barn med løpende fagsak`() {
        val sf = StonadFactory()
        val barn = sf.barn()
        val utbetaling = sf.utbetaling()
        val stonad = sf.stonad(
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
