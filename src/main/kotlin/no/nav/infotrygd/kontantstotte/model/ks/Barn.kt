package no.nav.infotrygd.kontantstotte.model.ks

import no.nav.commons.foedselsnummer.Foedselsnummer
import no.nav.infotrygd.kontantstotte.model.converters.BigDecimalFoedselsnummerConverter
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "KS_BARN_10")
data class Barn(
    @Id
    @Column(name = "ID_BARN", nullable = false)
    val id: BigDecimal,

    @Column(name = "REGION", columnDefinition = "CHAR")
    val region: String,

    @Column(name = "K01_PERSONKEY")
    val personkey: BigDecimal,

    @Column(name = "K10_BA_IVER_SEQ")
    val iverSeq: String,

    @Column(name = "K10_BA_VFOM_SEQ")
    val virkfomSeq: String,

    @Column(name = "K10_BARN_FNR")
    @Convert(converter = BigDecimalFoedselsnummerConverter::class)
    val fnr: Foedselsnummer
)