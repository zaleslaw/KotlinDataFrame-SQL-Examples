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
import org.jetbrains.kotlinx.dataframe.io.getSchemaForSqlTable
import org.jetbrains.kotlinx.dataframe.schema.DataFrameSchema

@DataSchema
interface Movies {
    val id: kotlin.Int
    val name: kotlin.String
    val year: kotlin.Int
    val rank: kotlin.Float?
}

fun main() {
    // define the database configuration
    val dbConfig = DbConnectionConfig(URL, USER_NAME, PASSWORD)

    val moviesSchema = DataFrame.getSchemaForSqlTable(dbConfig, TABLE_NAME_MOVIES)
    moviesSchema.printAsInterfaceWithAnnotation(TABLE_NAME_MOVIES)

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

fun String.convertToValidInterfaceName(): String {
    return this.split("_", " ")
        .joinToString("") { it.replaceFirstChar { char -> char.uppercaseChar() } }
}

fun DataFrameSchema.printAsInterfaceWithAnnotation(interfaceName: String? = null) {
    val schemaBuilder = StringBuilder()
    schemaBuilder.appendLine("@DataSchema")
    if(interfaceName != null) schemaBuilder.appendLine("interface ${interfaceName.convertToValidInterfaceName()} {")
    else schemaBuilder.appendLine("interface GeneratedSchema {")

    columns.forEach { (name, column) ->
        val type = column.type
        schemaBuilder.appendLine("    val $name: $type")
    }

    schemaBuilder.appendLine("}")

    println(schemaBuilder.toString())
}
