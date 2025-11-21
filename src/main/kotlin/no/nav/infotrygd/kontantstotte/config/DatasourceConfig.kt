package no.nav.infotrygd.kontantstotte.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Files
import java.nio.file.Paths
import jakarta.sql.DataSource

@Configuration
class DatasourceConfig {
    @Bean
    fun datasourceConfiguration(): DatasourceConfiguration = DatasourceConfiguration()

    @Bean
    fun vaultDatasourceUsername(
        @Value("\${vault.username}") filePath: String,
    ): String {
        val path = Paths.get(filePath)
        return Files.readString(path)
    }

    @Bean
    fun vaultDatasourcePassword(
        @Value("\${vault.password}") filePath: String,
    ): String {
        val path = Paths.get(filePath)
        return Files.readString(path)
    }

    @Bean
    fun datasource(
        datasourceConfiguration: DatasourceConfiguration,
        vaultDatasourceUsername: String,
        vaultDatasourcePassword: String,
    ): DataSource {
        requireNotNull(datasourceConfiguration.url) { "spring.datasource.url is null" }
        requireNotNull(datasourceConfiguration.driverClassName) { "spring.datasource.driverClassName is null" }
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName(datasourceConfiguration.driverClassName)
        dataSourceBuilder.url(datasourceConfiguration.url)
        dataSourceBuilder.username(vaultDatasourceUsername)
        dataSourceBuilder.password(vaultDatasourcePassword)
        return dataSourceBuilder.build()
    }
}

@ConfigurationProperties(prefix = "spring.datasource")
data class DatasourceConfiguration(
    var url: String? = null,
    var driverClassName: String? = null,
)
