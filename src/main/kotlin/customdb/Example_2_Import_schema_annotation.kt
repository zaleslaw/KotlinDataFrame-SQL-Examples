@file:ImportDataSchema(
    "OrdersImportedByAnnotation",
    URL,
    jdbcOptions = JdbcOptions(USER_NAME, PASSWORD, tableName = "orders", dbTypeKClass = HSQLDB::class)
)
package org.jetbrains.kotlinx.dataframe.examples.jdbc.customdb

import customdb.HSQLDB
import org.jetbrains.kotlinx.dataframe.annotations.ImportDataSchema
import org.jetbrains.kotlinx.dataframe.annotations.JdbcOptions
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.impl.DELIMITERS_REGEX
import org.jetbrains.kotlinx.dataframe.impl.toCamelCaseByDelimiters
import org.jetbrains.kotlinx.dataframe.io.readDataFrame
import java.sql.DriverManager
import java.util.*

fun main() {
    // Registering the HSQLDB JDBC driver
    Class.forName("org.hsqldb.jdbc.JDBCDriver")

    // Creating the connection with HSQLDB
    DriverManager.getConnection(URL, USER_NAME, PASSWORD).use { con ->
        createAndPopulateTable(con)

        val df = con.readDataFrame("SELECT * FROM orders", dbType = HSQLDB).rename {
            all()
        }.into { it.name.lowercase(Locale.getDefault()).toCamelCaseByDelimiters(DELIMITERS_REGEX)}
            .cast<Orders>(verify=true)

        df.filter { it.price > 800 }.print()

        removeTable(con)
    }
}
