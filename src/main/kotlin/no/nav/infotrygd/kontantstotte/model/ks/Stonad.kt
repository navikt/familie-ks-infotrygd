package no.nav.infotrygd.kontantstotte.model.ks

import no.nav.commons.foedselsnummer.Foedselsnummer
import no.nav.infotrygd.kontantstotte.model.converters.NavCharYearMonthConverter
import no.nav.infotrygd.kontantstotte.model.converters.ReversedFoedselNrConverter
import no.nav.infotrygd.kontantstotte.utils.DatoFormat
import no.nav.infotrygd.kontantstotte.utils.fromSeqToYearMonthOrNull
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import java.math.BigDecimal
import java.time.YearMonth
import jakarta.persistence.*

@Entity
@Table(name = "KS_STONAD_20")
class Stonad(
    @Id
    @Column(name = "ID_STND", nullable = false)
    val id: BigDecimal,

    @Column(name = "REGION", columnDefinition = "CHAR")
    val region: String,

    @Column(name = "K01_PERSONKEY")
    val personkey: BigDecimal,

    @Column(name = "K20_IVERFOM_SEQ")
    val iverfomSeq: String,

    @Column(name = "K20_VIRKFOM_SEQ")
    val virkfomSeq: String,

    @Column(name = "K20_OPPHOERT_VFOM")
    @Convert(converter = NavCharYearMonthConverter::class)
    val opphoertVfom: YearMonth?,

    @Column(name = "F_NR", columnDefinition = "CHAR")
    @Convert(converter = ReversedFoedselNrConverter::class)
    val fnr: Foedselsnummer,

    @OneToMany
    @JoinColumns(value = [
        JoinColumn(name = "REGION", referencedColumnName = "REGION"),
        JoinColumn(name = "K01_PERSONKEY", referencedColumnName = "K01_PERSONKEY"),
        JoinColumn(name = "K10_BA_IVER_SEQ", referencedColumnName = "K20_IVERFOM_SEQ"),
        JoinColumn(name = "K10_BA_VFOM_SEQ", referencedColumnName = "K20_VIRKFOM_SEQ")
    ])
    @Cascade(value = [CascadeType.ALL])
    @BatchSize(size = 100)
    val barn: List<Barn>,

    @OneToMany
    @JoinColumns(value = [
        JoinColumn(name = "REGION", referencedColumnName = "REGION"),
        JoinColumn(name = "K01_PERSONKEY", referencedColumnName = "K01_PERSONKEY"),
        JoinColumn(name = "K30_START_UTBET_MND_SEQ", referencedColumnName = "K20_IVERFOM_SEQ"),
        JoinColumn(name = "K30_VFOM_SEQ", referencedColumnName = "K20_VIRKFOM_SEQ")
    ])
    @Cascade(value = [CascadeType.ALL])
    @BatchSize(size = 100)
    val utbetalinger: List<Utbetaling>
) : java.io.Serializable {
    val fom: YearMonth?
        get() = fromSeqToYearMonthOrNull(virkfomSeq, DatoFormat.YYYYMM)

    val tom: YearMonth?
        get() = opphoertVfom?.minusMonths(1)

    val belop: Int?
        get() = utbetalinger.filter { it.type.trim() == "M" }.firstOrNull()?.belop
}