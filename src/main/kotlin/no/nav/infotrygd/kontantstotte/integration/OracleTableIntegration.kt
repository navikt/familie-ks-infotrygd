package no.nav.infotrygd.kontantstotte.integration

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.sql.Connection
import javax.sql.DataSource

data class OracleTable(
    val owner: String,
    val tableName: String,
)

@Component
class OracleTableIntegration(
    private val dataSource: DataSource,
    @Value("\${APP_DEFAULT_SCHEMA}") private val schema: String,
) {
    fun getTables(): List<OracleTable> =
        dataSource.connection.use { connection ->
            connection.queryTables(schema)
        }
}

private fun Connection.queryTables(schema: String): List<OracleTable> =
    prepareStatement(
        """
        SELECT OWNER, TABLE_NAME
        FROM ALL_TABLES
        WHERE OWNER = ?
        ORDER BY TABLE_NAME
        """.trimIndent(),
    ).use { statement ->
        statement.setString(1, schema)
        statement.executeQuery().use { resultSet ->
            buildList {
                while (resultSet.next()) {
                    add(
                        OracleTable(
                            owner = resultSet.getString("OWNER"),
                            tableName = resultSet.getString("TABLE_NAME"),
                        ),
                    )
                }
            }
        }
    }
