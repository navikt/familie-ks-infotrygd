package no.nav.infotrygd.kontantstotte.testutil

import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(initializers = [MockOAuth2ServerInitializer::class])
open class AbstractStonadFactoryTest {
    val sf = StonadFactory()
}
