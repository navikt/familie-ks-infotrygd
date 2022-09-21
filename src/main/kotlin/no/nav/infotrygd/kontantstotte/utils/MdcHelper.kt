package no.nav.infotrygd.kontantstotte.utils

import org.slf4j.MDC

object MdcHelper {
    const val MDC_CONSUMER_ID = "consumerId" // TODO
    const val MDC_CALL_ID = "callId"

    var callId: String?
        get() = MDC.get(MDC_CALL_ID)
        set(value) = MDC.put(MDC_CALL_ID, value)

    var consumerId: String?
        get() = MDC.get(MDC_CONSUMER_ID)
        set(value) = MDC.put(MDC_CONSUMER_ID, value)

    fun clear() {
        MDC.clear()
    }
}