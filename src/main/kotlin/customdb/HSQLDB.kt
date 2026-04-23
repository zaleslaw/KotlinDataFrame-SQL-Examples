package org.jetbrains.kotlinx.dataframe.examples.jdbc.customdb

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.examples.jdbc.customdb.HSQLDB.getPreprocessedValueType
import org.jetbrains.kotlinx.dataframe.examples.jdbc.customdb.HSQLDB.preprocessValue
import org.jetbrains.kotlinx.dataframe.io.db.DbType
import org.jetbrains.kotlinx.dataframe.io.db.TableColumnMetadata
import org.jetbrains.kotlinx.dataframe.io.db.TableMetadata
import java.sql.ResultSet
import java.sql.Types
import kotlin.reflect.KType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.withNullability
import kotlin.reflect.typeOf
import java.sql.Date as SqlDate

/**
 * Represents the HSQLDB database type.
 *
 * This object provides all functions to read data from a HSQLDB [ResultSet],
 * preprocess the values, and build [columns][DataColumn].
 */
object HSQLDB : DbType("hsqldb") {

    /** The JDBC driver class name. */
    override val driverClassName: String = "org.hsqldb.jdbcDriver"

    /**
     * This function should return the correct type of the value returned by [ResultSet.getObject] from JDBC
     * for the column with the given [tableColumnMetadata].
     * [DbType] has a good default type-map, but your database type might deviate.
     *
     * Supplying these types helps you and DataFrame to correctly interpret and handle data from the database.
     */
    override fun getExpectedJdbcType(tableColumnMetadata: TableColumnMetadata): KType =
        when (tableColumnMetadata.jdbcType) {
            // For example, here we say that we expect .getObject() to return a Java SQL Date
            // when the given JDBC SQL type is DATE
            Types.DATE -> typeOf<SqlDate>().withNullability(tableColumnMetadata.isNullable)

            // TODO this list is likely incomplete for HSQLDB

            // Else, we follow the default behavior
            else -> super.getExpectedJdbcType(tableColumnMetadata)
        }

    /**
     * If you want to preprocess certain values before creating a [DataColumn], you can override this function.
     * [DbType] already has a few types of values being preprocessed, but you can customize this behavior.
     *
     * This function just specifies the type-behavior, [preprocessValue] actually does the preprocessing.
     */
    override fun getPreprocessedValueType(
        tableColumnMetadata: TableColumnMetadata,
        expectedJdbcType: KType
    ): KType =
        when {
            // Let's say we want to convert java.sql.Date to kotlinx.datetime.LocalDate (taking nullability into account)
            expectedJdbcType.isSubtypeOf(typeOf<SqlDate?>()) ->
                typeOf<LocalDate>().withNullability(tableColumnMetadata.isNullable)

            // Else, we follow the default behavior
            else ->
                super.getPreprocessedValueType(tableColumnMetadata, expectedJdbcType)
        }

    /**
     * This function actually preprocesses the values returned by [ResultSet.getObject], following the
     * [getPreprocessedValueType] type-behavior.
     */
    override fun <J, D> preprocessValue(
        value: J,
        tableColumnMetadata: TableColumnMetadata,
        expectedJdbcType: KType,
        expectedPreprocessedValueType: KType
    ): D = when {
        // Here we actually perform the conversion from java.sql.Date to kotlinx.datetime.LocalDate
        expectedJdbcType.isSubtypeOf(typeOf<SqlDate?>()) ->
            (value as SqlDate?)?.toLocalDate()?.toKotlinLocalDate() as D

        // Else, we follow the default behavior
        else ->
            super.preprocessValue(value, tableColumnMetadata, expectedJdbcType, expectedPreprocessedValueType)
    }

    override fun isSystemTable(tableMetadata: TableMetadata): Boolean {
        val schemaName = tableMetadata.schemaName.orEmpty().lowercase()
        val name = tableMetadata.name.lowercase()

        return "information_schema" in schemaName ||
                "system" in schemaName ||
                "system_" in name
    }

    override fun buildTableMetadata(tables: ResultSet): TableMetadata =
        TableMetadata(
            name = tables.getString("TABLE_NAME"),
            schemaName = tables.getString("TABLE_SCHEM"),
            catalogue = tables.getString("TABLE_CAT"),
        )
}
