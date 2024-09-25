@file:ImportDataSchema(
    "Directors",
    URL,
    jdbcOptions = JdbcOptions(USER_NAME, PASSWORD, tableName = TABLE_NAME_DIRECTORS)
)

@file:ImportDataSchema(
    "NewActors",
    URL,
    jdbcOptions = JdbcOptions(USER_NAME, PASSWORD, sqlQuery = ACTORS_IN_LATEST_MOVIES)
)

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.annotations.ImportDataSchema
import org.jetbrains.kotlinx.dataframe.annotations.JdbcOptions
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.filter
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.groupBy
import org.jetbrains.kotlinx.dataframe.api.count
import org.jetbrains.kotlinx.dataframe.io.readSqlQuery
import org.jetbrains.kotlinx.dataframe.io.readSqlTable
import org.jetbrains.kotlinx.dataframe.api.take
import org.jetbrains.kotlinx.dataframe.examples.jdbc.ACTORS_IN_LATEST_MOVIES
//import org.jetbrains.kotlinx.dataframe.examples.jdbc.Directors
//import org.jetbrains.kotlinx.dataframe.examples.jdbc.NewActors
import org.jetbrains.kotlinx.dataframe.examples.jdbc.PASSWORD
import org.jetbrains.kotlinx.dataframe.examples.jdbc.TABLE_NAME_DIRECTORS
import org.jetbrains.kotlinx.dataframe.examples.jdbc.URL
import org.jetbrains.kotlinx.dataframe.examples.jdbc.USER_NAME
import org.jetbrains.kotlinx.dataframe.io.DbConnectionConfig

fun main() {
    // Part 1: Reading the table `directors`

    // define the database configuration
  /*  val dbConfig = DbConnectionConfig(URL, USER_NAME, PASSWORD)

    // read the table
    val directors = DataFrame.readSqlTable(dbConfig, TABLE_NAME_DIRECTORS, 1000).cast<Directors>()

    // manipulate and print
    directors.filter { firstName != null && firstName!!.contains("A") }
        .take(10)
        .print()

    // Part 2: Handle the table results of an SQL query

    // read the data
    val newActors = DataFrame.readSqlQuery(dbConfig, ACTORS_IN_LATEST_MOVIES).cast<NewActors>()
    newActors.print()

    // build a report of different roles' popularity
    newActors.groupBy { role }
        .count()
        .print()*/
}
