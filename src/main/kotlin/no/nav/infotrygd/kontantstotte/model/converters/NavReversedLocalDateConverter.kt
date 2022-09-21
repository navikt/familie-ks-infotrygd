package no.nav.infotrygd.kontantstotte.model.converters

import javax.persistence.Converter

@Converter
class NavReversedLocalDateConverter : AbstractNavLocalDateConverter("ddMMyyyy")