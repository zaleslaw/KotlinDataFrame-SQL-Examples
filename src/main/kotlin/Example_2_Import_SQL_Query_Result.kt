package org.jetbrains.kotlinx.dataframe.examples.jdbc

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.annotations.ColumnName
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.filter
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.groupBy
import org.jetbrains.kotlinx.dataframe.api.count
import org.jetbrains.kotlinx.dataframe.api.generateInterfaces
import org.jetbrains.kotlinx.dataframe.io.readSqlQuery
import org.jetbrains.kotlinx.dataframe.io.readSqlTable
import org.jetbrains.kotlinx.dataframe.api.take
import org.jetbrains.kotlinx.dataframe.io.DbConnectionConfig

@DataSchema
interface Directors {
    @ColumnName("first_name")
    val firstName: String
    val id: Int
    @ColumnName("last_name")
    val lastName: String
}

@DataSchema
interface Actors {
    @ColumnName("first_name")
    val firstName: String
    @ColumnName("last_name")
    val lastName: String
    val name: String
    val role: String
    val year: Int
}

fun main() {
    // Part 1: Reading the table `directors`

    // define the database configuration
    val dbConfig = DbConnectionConfig(URL, USER_NAME, PASSWORD)

    // read the table
    val df1 = DataFrame.readSqlTable(dbConfig, TABLE_NAME_DIRECTORS, 1000)
    df1.generateInterfaces("Directors").print()

    val directors = df1.cast<Directors>()

    // manipulate and print
    directors.filter { firstName.contains("A") }
        .take(10)
        .print()

    // Part 2: Handle the table results of an SQL query

    // read the data
    val df2 = DataFrame.readSqlQuery(dbConfig, ACTORS_IN_LATEST_MOVIES)
    df2.generateInterfaces("Actors").print()

    val actors = df2.cast<Actors>()
    actors.print()

    // build a report of different roles' popularity
    actors.groupBy { role }
        .count()
        .print()
}
