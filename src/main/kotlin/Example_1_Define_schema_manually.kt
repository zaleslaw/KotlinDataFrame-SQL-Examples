import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema
import org.jetbrains.kotlinx.dataframe.api.add
import org.jetbrains.kotlinx.dataframe.api.describe
import org.jetbrains.kotlinx.dataframe.api.select
import org.jetbrains.kotlinx.dataframe.api.cast
import org.jetbrains.kotlinx.dataframe.api.filter
import org.jetbrains.kotlinx.dataframe.api.sortByDesc
import org.jetbrains.kotlinx.dataframe.api.print
import org.jetbrains.kotlinx.dataframe.api.take
import org.jetbrains.kotlinx.dataframe.examples.jdbc.PASSWORD
import org.jetbrains.kotlinx.dataframe.examples.jdbc.TABLE_NAME_MOVIES
import org.jetbrains.kotlinx.dataframe.examples.jdbc.URL
import org.jetbrains.kotlinx.dataframe.examples.jdbc.USER_NAME
import org.jetbrains.kotlinx.dataframe.io.readSqlTable
import org.jetbrains.kotlinx.dataframe.io.DbConnectionConfig

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
    val result = movies.sortByDesc { rank }
        .select { name and year }
        .add("oldFilm") { year < 1973 }
        .add("containsReward") { name.contains("Reward") }

    result.filter { oldFilm and containsReward }.take(10).print()
}
