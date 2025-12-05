package org.jetbrains.kotlinx.dataframe.examples.jdbc.customdb

/*import org.jetbrains.kotlinx.dataframe.io.TableColumnMetadata
import org.jetbrains.kotlinx.dataframe.io.TableMetadata
import org.jetbrains.kotlinx.dataframe.io.db.DbType
import org.jetbrains.kotlinx.dataframe.schema.ColumnSchema
import java.sql.ResultSet
import java.util.Locale
import kotlin.reflect.KType*/

/**
 * Represents the HSQLDB database type.
 *
 * This class provides methods to convert data from a ResultSet to the appropriate type for HSQLDB,
 * and to generate the corresponding column schema.
 */
/*public object HSQLDB : DbType("hsqldb") {
    override val driverClassName: String
        get() = "org.hsqldb.jdbcDriver"

    override fun convertSqlTypeToColumnSchemaValue(tableColumnMetadata: TableColumnMetadata): ColumnSchema? {
        return null
    }

    override fun isSystemTable(tableMetadata: TableMetadata): Boolean {
        val locale = Locale.getDefault()
        fun String?.containsWithLowercase(substr: String) = this?.lowercase(locale)?.contains(substr) == true
        val schemaName = tableMetadata.schemaName
        val name = tableMetadata.name
        return schemaName.containsWithLowercase("information_schema") ||
                schemaName.containsWithLowercase("system") ||
                name.containsWithLowercase("system_")
    }

    override fun buildTableMetadata(tables: ResultSet): TableMetadata =
        TableMetadata(
            tables.getString("TABLE_NAME"),
            tables.getString("TABLE_SCHEM"),
            tables.getString("TABLE_CAT"),
        )

    override fun convertSqlTypeToKType(tableColumnMetadata: TableColumnMetadata): KType? {
        return null
    }
}
*/