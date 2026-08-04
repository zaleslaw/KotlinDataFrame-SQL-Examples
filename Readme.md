# Kotlin DataFrame ↔ SQL Examples

**Query a SQL database and get back a fully typed Kotlin [DataFrame](https://github.com/Kotlin/dataframe) — then filter, group, add columns, and plot the result, all in idiomatic Kotlin.**

No ORM, no boilerplate mapping. Point DataFrame at a table or a raw SQL query and it reads the data,
infers the schema, and lets you generate `@DataSchema` interfaces for type-safe, autocomplete-friendly
access to every column.

```kotlin
val df = DataFrame.readSqlTable(dbConfig, "movies", limit = 10_000).cast<Movies>()

df.sortByDesc { rank }
    .select { name and year }
    .add("oldFilm") { year < 1973 }
    .filter { oldFilm }
    .take(10)
    .print()
```

Built with Kotlin `2.4.20-Beta1` · Kotlin DataFrame `1.0.0-rc01` · [Kandy](https://kotlin.github.io/kandy/) `0.8.5` · MariaDB & HSQLDB JDBC · JVM 11.

---

## What you'll learn

- Read a whole SQL table into a `DataFrame` (`readSqlTable`)
- Run an arbitrary SQL query and get a `DataFrame` back (`readSqlQuery`)
- Read straight from a JDBC `ResultSet` (`readResultSet`)
- Slurp **every** table in a database at once (`readAllSqlTables`)
- Auto-generate typed schema interfaces (`generateInterfaces`) and `cast` to them
- Map column names with `@ColumnName`, explore data with `describe`, and transform with
  `filter` / `sortByDesc` / `groupBy` / `add`
- Visualize results in Kotlin Notebooks with Kandy / Lets-Plot
- **Advanced:** teach DataFrame about a database it doesn't support out of the box by implementing
  your own `DbType` (see [Advanced](#advanced-support-a-custom-database))

---

## Getting started (MariaDB + the IMDb dataset)

The examples query an `imdb` database (movies, actors, directors, roles, genres).

1. **Install MariaDB** — instructions [here](https://mariadb.com/kb/en/download/).
2. **Download the SQL dump** for the `imdb` database from
   [this link](https://drive.google.com/file/d/10HnOu0Yem2Tkz_34SfvDoHTVqF_8b4N7/view?usp=sharing).
3. **Create the database and load the dump:**
   ```bash
   mysql -u root -p -e "CREATE DATABASE imdb;"
   mysql -u root -p imdb < imdb_dump.sql
   ```
4. **Check the connection settings** in [`src/main/kotlin/jdbcUtils.kt`](src/main/kotlin/jdbcUtils.kt)
   and adjust them to match your setup if needed:
   ```kotlin
   const val URL      = "jdbc:mariadb://localhost:3307/imdb"
   const val USER_NAME = "root"
   const val PASSWORD  = "pass"
   ```
5. Open the project in IntelliJ IDEA and run any of the `main()` functions below by clicking the
   green ▶ gutter icon next to it.

---

## Examples overview

| File | What it demonstrates |
|------|----------------------|
| [`Example_1_Add_Column.kt`](src/main/kotlin/Example_1_Add_Column.kt) | Read the `movies` table, `generateInterfaces`, `cast` to a `@DataSchema`, run `describe`, sort by rank, and `add` computed columns on the fly. |
| [`Example_2_Import_SQL_Query_Result.kt`](src/main/kotlin/Example_2_Import_SQL_Query_Result.kt) | Read a table **and** a raw SQL query (`readSqlQuery`), map columns with `@ColumnName`, and build a role-popularity report with `groupBy { role }.count()`. |
| [`Example_3_API_Demonstration.kt`](src/main/kotlin/Example_3_API_Demonstration.kt) | Four ways into the data: from a JDBC `Connection`, from a SQL query, straight from a `ResultSet`, and `readAllSqlTables` to read every table at once — plus extracting the `DataFrameSchema`. |

---

## Notebooks

Interactive versions with charts live in [`notebooks/`](notebooks):

- [`notebooks/imdb.ipynb`](notebooks/imdb.ipynb) — explore the IMDb data and plot it with Kandy.
- [`notebooks/customdb.ipynb`](notebooks/customdb.ipynb) — the custom `DbType` walkthrough.

Open them in IntelliJ IDEA (with the Kotlin Notebook plugin) or in Jupyter with the
[Kotlin kernel](https://github.com/Kotlin/kotlin-jupyter). Rendered plot images are in
`notebooks/lets-plot-images/`.

---

## Advanced: support a custom database

Kotlin DataFrame ships with built-in support for the most common databases. But what if you're on a
database it doesn't recognize out of the box? The `customdb` example shows how to plug your own
database into DataFrame's architecture by implementing a custom `DbType` — using **HSQLDB** as a
worked example.

[`customdb/HSQLDB.kt`](src/main/kotlin/customdb/HSQLDB.kt) implements `DbType` and overrides the hooks
that let DataFrame read from an unfamiliar database correctly:

- `getExpectedJdbcType` — declare what `ResultSet.getObject` actually returns for a given JDBC type.
- `getPreprocessedValueType` / `preprocessValue` — convert raw JDBC values into richer Kotlin types
  (here: `java.sql.Date` → `kotlinx.datetime.LocalDate`).
- `isSystemTable` / `buildTableMetadata` — control table discovery for `readAllSqlTables`.

[`customdb/Example_1_Simple_Example.kt`](src/main/kotlin/customdb/Example_1_Simple_Example.kt) then uses
that `DbType` to create a table, insert rows, and read them into a typed DataFrame. It's also handy as a
**self-contained demo**: it needs no external dump — just an HSQLDB server, or switch `val url = URL`
to `val url = URL_IN_MEMORY` in `main()` to run entirely in memory. Constants and DDL live in
[`customdb/hsqldbUtils.kt`](src/main/kotlin/customdb/hsqldbUtils.kt).

---

## Project structure

```
KotlinDataFrame-SQL-Examples/
├── build.gradle.kts
├── src/main/kotlin/
│   ├── jdbcUtils.kt                    # MariaDB connection constants & SQL queries
│   ├── Example_1_Add_Column.kt
│   ├── Example_2_Import_SQL_Query_Result.kt
│   ├── Example_3_API_Demonstration.kt
│   └── customdb/                       # advanced: custom DbType support
│       ├── Example_1_Simple_Example.kt
│       ├── HSQLDB.kt                   # custom DbType implementation
│       └── hsqldbUtils.kt              # HSQLDB connection constants & DDL
└── notebooks/
    ├── imdb.ipynb
    └── customdb.ipynb
```

---

## Learn more

- [Kotlin DataFrame](https://github.com/Kotlin/dataframe) · [documentation](https://kotlin.github.io/dataframe/)
- [Reading from SQL databases](https://kotlin.github.io/dataframe/readsqldatabases.html)
- [Kandy](https://kotlin.github.io/kandy/) — Kotlin plotting library used in the notebooks
