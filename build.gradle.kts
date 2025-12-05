import java.util.*

plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.dataframe") version "2.2.20"
    id("org.jetbrains.kotlinx.dataframe") version "1.0.0-Beta4"
}

group = "org.jetbrains.kotlinx.dataframe.examples"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.jetbrains.kotlinx:dataframe:1.0.0-Beta4")
    implementation ("org.jetbrains.kotlinx:kandy-lets-plot:0.8.1-dev-67")
    implementation ("org.jetbrains.kotlinx:kandy-api:0.8.1-dev-67")
    implementation ("org.mariadb.jdbc:mariadb-java-client:3.5.4")
    implementation ("org.hsqldb:hsqldb:2.7.4")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}