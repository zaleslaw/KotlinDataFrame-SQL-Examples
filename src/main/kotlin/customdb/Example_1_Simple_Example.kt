package org.jetbrains.kotlinx.dataframe.examples.jdbc.customdb

import org.jetbrains.kotlinx.dataframe.annotations.DataSchema
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.readDataFrame
import java.sql.DriverManager
import java.util.*

@DataSchema
interface Orders {
    val id: Int
    val item: String
    val price: Double
    val orderDate: java.util.Date
}

//https://www.tutorialspoint.com/hsqldb/hsqldb_quick_guide.htm
// CMD> java -classpath lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:hsqldb/demodb --dbname.0 testdb
fun main() {
    /*DriverManager.getConnection(URL, USER_NAME, PASSWORD).use { con ->
        createAndPopulateTable(con)

        val df = con
            .readDataFrame("SELECT * FROM orders", dbType = HSQLDB)
            .renameToCamelCase()
            .cast<Orders>(verify = true)

       // df.filter { it.price > 800 }.print()

        removeTable(con)
    }*/
}
