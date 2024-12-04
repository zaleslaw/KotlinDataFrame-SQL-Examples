package org.jetbrains.kotlinx.dataframe.examples.jdbc.customdb

import java.sql.Connection

const val URL = "jdbc:hsqldb:hsql://localhost/testdb"
const val USER_NAME = "SA"
const val PASSWORD = ""


fun removeTable(con: Connection): Int {
    val stmt = con.createStatement()
    return stmt.executeUpdate("""DROP TABLE orders""")
}

fun createAndPopulateTable(con: Connection) {
    var stmt = con.createStatement()
    stmt.executeUpdate(
        """CREATE TABLE IF NOT EXISTS orders (
                        id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                        item VARCHAR(50) NOT NULL,
                        price DOUBLE NOT NULL,
                        order_date DATE
                    );
                """.trimIndent()
    )

    stmt.executeUpdate(
        """INSERT INTO orders (item, price, order_date) 
                        VALUES ('Laptop', 1500.00, NOW())""".trimIndent()
    )

    stmt.executeUpdate(
        """INSERT INTO orders (item, price, order_date) 
                        VALUES ('Smartphone', 700.00, NOW())""".trimIndent()
    )
}