package org.jetbrains.kotlinx.dataframe.examples.jdbc

import java.sql.DriverManager
import java.util.Properties
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.describe
import org.jetbrains.kotlinx.dataframe.api.filter
import org.jetbrains.kotlinx.dataframe.api.generateInterfaces
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.io.db.MySql
import org.jetbrains.kotlinx.dataframe.io.readAllSqlTables
import org.jetbrains.kotlinx.dataframe.io.readResultSet
import org.jetbrains.kotlinx.dataframe.io.readSqlQuery
import org.jetbrains.kotlinx.dataframe.io.readSqlTable
import org.jetbrains.kotlinx.dataframe.schema.DataFrameSchema

@DataSchema
interface TarantinoFilms {
    val genres: String?
    val name: String
    val rank: Float?
    val year: Int
}

fun main() {
    val props = Properties()
    props.setProperty("user", USER_NAME)
    props.setProperty("password", PASSWORD)

    // Part 1: Getting the data from the SQL table with an explicit announcement of the Connection object from the JDBC driver.
    println("---------------------------- Part 1: SQL Table ------------------------------------")
    DriverManager.getConnection(URL, props).use { connection ->
        // read the data from the SQL table
        val actors = DataFrame.readSqlTable(connection, TABLE_NAME_ACTORS, 100).cast<Actors>()
        actors.print()

        // filter and print the data
        actors.filter { firstName.contains("J") }.print()

        // extract the schema of the SQL table
        val actorSchema = DataFrameSchema.readSqlTable(connection, TABLE_NAME_ACTORS)
        actorSchema.print()
    }

    // Part 2: Getting the data from the SQL query with an explicit announcement of the Connection object from the JDBC driver.
    println("---------------------------- Part 2: SQL Query ------------------------------------")
    DriverManager.getConnection(URL, props).use { connection ->
        // read the data from as a result of an executed SQL query
        val tarantinoFilmsUntyped = DataFrame.readSqlQuery(connection, TARANTINO_FILMS_SQL_QUERY, 100)
        tarantinoFilmsUntyped.generateInterfaces("TarantinoFilms").print()

        val tarantinoFilms = tarantinoFilmsUntyped.cast<TarantinoFilms>()

        tarantinoFilms.print()

        // transform and print the data
        tarantinoFilms.filter { year > 2000 }.print()

        // extract the schema of the SQL table
        val tarantinoFilmsSchema = DataFrameSchema.readSqlQuery(connection, TARANTINO_FILMS_SQL_QUERY)
        tarantinoFilmsSchema.print()
    }

    // Part 3: Getting the data from the SQL query with an explicit announcement of the ResultSet object from the JDBC driver.
    println("---------------------------- Part 3: ResultSet ------------------------------------")
    DriverManager.getConnection(URL, props).use { connection ->
        connection.createStatement().use { st ->
            st.executeQuery(TARANTINO_FILMS_SQL_QUERY).use { rs ->
                // read the data from as a result of an executed SQL query
                val tarantinoFilms = DataFrame.readResultSet(rs, dbType = MySql, 100).cast<TarantinoFilms>()

                tarantinoFilms.print()
                // transform and print the data
                tarantinoFilms.filter { year > 2000 }.print()

                // extract the schema of the SQL table
                val tarantinoFilmsSchema = DataFrameSchema.readResultSet(rs, dbType = MySql)
                tarantinoFilmsSchema.print()
            }
        }
    }

    // Part 4: Getting the bunch of dataframes (one per each non-system SQL table)
    // with an explicit announcement of the Connection object from the JDBC driver.
    println("---------------------------- Part 4: readAllSqlTables ------------------------------------")
    DriverManager.getConnection(URL, props).use { connection ->
        val dataFrames = DataFrame.readAllSqlTables(connection, limit = 100).values
        dataFrames.forEach {
            it.print()
            it.describe()
        }
    }
}
