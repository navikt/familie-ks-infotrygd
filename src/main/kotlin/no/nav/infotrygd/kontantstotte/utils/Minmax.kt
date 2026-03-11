package no.nav.infotrygd.kontantstotte.utils

fun <T : Comparable<T>> min(
    a: T,
    b: T,
): T = if (a < b) a else b

fun <T : Comparable<T>> max(
    a: T,
    b: T,
): T = if (a > b) a else b
