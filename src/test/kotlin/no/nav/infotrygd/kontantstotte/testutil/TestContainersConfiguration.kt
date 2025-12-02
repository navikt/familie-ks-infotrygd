package no.nav.infotrygd.kontantstotte.testutil

import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.testcontainers.oracle.OracleContainer
import org.testcontainers.utility.DockerImageName
import javax.sql.DataSource

@TestConfiguration
class TestContainersConfiguration {

    companion object {
        private val oracleContainer: OracleContainer = OracleContainer(
            DockerImageName.parse("gvenzl/oracle-free:latest")
        ).apply {
            withReuse(true)
            start()
        }
    }

    @Bean
    @Primary
    fun datasource(): DataSource {
        return DataSourceBuilder.create()
            .url(oracleContainer.jdbcUrl)
            .username(oracleContainer.username)
            .password(oracleContainer.password)
            .driverClassName("oracle.jdbc.OracleDriver")
            .build()
    }

    @Bean
    fun vaultDatasourceUsername(): String = oracleContainer.username

    @Bean
    fun vaultDatasourcePassword(): String = oracleContainer.password
}
