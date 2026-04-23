package org.jetbrains.kotlinx.dataframe.examples.jdbc.customdb

import kotlinx.datetime.LocalDate
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.filter
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.renameToCamelCase
import org.jetbrains.kotlinx.dataframe.io.readDataFrame
import java.sql.DriverManager

@DataSchema
interface Orders {
    val id: Int
    val item: String
    val price: Double
    val orderDate: LocalDate
}


/**
 * Following https://www.tutorialspoint.com/hsqldb/hsqldb_quick_guide.htm:
 *
 * Run `CMD> java -classpath lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:hsqldb/demodb --dbname.0 testdb`
 *
 * Use `url = `[URL] to connect to the running server.
 *
 * Alternatively, use `url = `[URL_IN_MEMORY] to connect to an in-memory database for testing purposes.
 */
fun main() {
    val url = URL
    DriverManager.getConnection(url, USER_NAME, PASSWORD).use { con ->
        createAndPopulateTable(con)

        val df = con
            .readDataFrame("SELECT * FROM orders", dbType = HSQLDB)
            .renameToCamelCase()
            .cast<Orders>(verify = true)

        df.filter { it.price > 800 }.print()

        removeTable(con)
    }
}
