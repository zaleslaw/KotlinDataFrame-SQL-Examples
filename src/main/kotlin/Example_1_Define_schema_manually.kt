package org.jetbrains.kotlinx.dataframe.examples.jdbc

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema
import org.jetbrains.kotlinx.dataframe.api.describe
import org.jetbrains.kotlinx.dataframe.api.select
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.sortByDesc
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.take
import org.jetbrains.kotlinx.dataframe.io.DbConnectionConfig
import org.jetbrains.kotlinx.dataframe.io.readSqlTable

@DataSchema
interface Movies {
    val id: Int
    val name: String
    val year: Int
    val rank: Float?
}

fun main() {
    // define the database configuration
    val dbConfig = DbConnectionConfig(URL, USER_NAME, PASSWORD)

    // read the table
    val movies = DataFrame.readSqlTable(dbConfig, TABLE_NAME_MOVIES, 10000).cast<Movies>(verify=true)

    // print the dataframe
    movies.print()

    // print the dataframe metadata and statistics
    movies.describe().print()

    // print names of top-10 rated films
    movies.sortByDesc { rank }
        .take(10)
        .select { name }
        .print()
}
