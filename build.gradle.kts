import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.21"
    antlr
    application

    id("de.undercouch.download") version "5.4.0"
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")

    implementation("dev.forkhandles:result4k:2.6.0.0")

    implementation(platform("org.http4k:http4k-bom:4.44.1.0"))
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-server-undertow")
    implementation("org.http4k:http4k-client-apache")
    implementation("org.http4k:http4k-testing-webdriver")
    implementation("org.http4k:http4k-template-handlebars")

    implementation("com.squareup:kotlinpoet:1.13.2")
    implementation("dev.forkhandles:result4k:2.5.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation(kotlin("reflect"))

    implementation("com.natpryce:konfig:1.6.10.0")

    antlr("org.antlr:antlr4:4.9.3")
}

tasks.named("compileKotlin")
    .configure { dependsOn("generateGrammarSource") }

tasks.named("compileTestKotlin")
    .configure { dependsOn("generateTestGrammarSource") }

tasks.test {
    useJUnitPlatform()
    testLogging {
        events(TestLogEvent.FAILED, TestLogEvent.STANDARD_ERROR, TestLogEvent.STANDARD_OUT)
        exceptionFormat = TestExceptionFormat.FULL
        showCauses = true
        showExceptions = true
    }
}

kotlin {
    jvmToolchain(11)
}

task<Exec>("tailwindWatch") {
    commandLine(
        "./tailwindcss",
        "-i",
        "./src/main/css/input.css",
        "-o",
        "./src/main/resources/public/output.css",
        "--watch=always",
    )
}

task<Exec>("tailwindBuild") {
    commandLine(
        "./tailwindcss",
        "-i",
        "./src/main/css/input.css",
        "-o",
        "./src/main/resources/public/output.css",
        "--minify",
    )
}

application {
    mainClass.set("MainKt")
}

