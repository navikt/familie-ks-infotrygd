package no.nav.infotrygd.kontantstotte.model.converters

import javax.persistence.Converter

@Converter
class NavLocalDateConverter : AbstractNavLocalDateConverter("yyyyMMdd")