plugins {
    kotlin("jvm") version "2.3.10"
    kotlin("plugin.dataframe") version "2.3.10"
    application
}

group = "org.jetbrains.kotlinx.dataframe.examples"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:dataframe:1.0.0-dev-9593")
    implementation("org.jetbrains.kotlinx:kandy-lets-plot:0.8.3-dev-94")
    implementation("org.jetbrains.kotlinx:kandy-api:0.8.3-dev-94")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.4")
    implementation("org.hsqldb:hsqldb:2.7.4")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}