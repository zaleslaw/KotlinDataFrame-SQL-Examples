import java.util.*

plugins {
    kotlin("jvm") version "2.0.0"
    id("org.jetbrains.kotlinx.dataframe") version "1.0.0-Beta2"
}

group = "org.jetbrains.kotlinx.dataframe.examples"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.jetbrains.kotlinx:dataframe: 1.0.0-Beta2")
    implementation ("org.jetbrains.kotlinx:kandy-lets-plot:0.8.1-dev-67")
    implementation ("org.jetbrains.kotlinx:kandy-api:0.8.1-dev-67")
    implementation ("org.mariadb.jdbc:mariadb-java-client:3.5.4")
    implementation ("org.hsqldb:hsqldb:2.7.4")

    testImplementation(kotlin("test"))
}


val props = Properties()
file("local.properties").inputStream().use { props.load(it) }

dataframes {
    schema {
        data = "jdbc:mariadb://localhost:3307/imdb"
        name = "org.jetbrains.kotlinx.dataframe.examples.jdbc.Actors"
        jdbcOptions {
            user = props.getProperty("db.user")
            password = props.getProperty("db.password")
            tableName = "actors"
        }
    }
    schema {
        data = "jdbc:mariadb://localhost:3307/imdb"
        name = "org.jetbrains.kotlinx.dataframe.examples.jdbc.TarantinoFilms"
        jdbcOptions {
            user = System.getenv("DB_USER") ?: props.getProperty("db.user")
            password = System.getenv("DB_PASSWORD") ?: props.getProperty("db.password")
            sqlQuery = """
                SELECT name, year, rank,
                GROUP_CONCAT (genre) as "genres"
                FROM movies JOIN movies_directors ON movie_id = movies.id
                JOIN directors ON directors.id=director_id LEFT JOIN movies_genres ON movies.id = movies_genres.movie_id
                WHERE directors.first_name = "Quentin" AND directors.last_name = "Tarantino"
                GROUP BY name, year, rank
                ORDER BY year
                """
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}