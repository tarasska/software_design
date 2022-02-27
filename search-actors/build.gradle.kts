import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
}

group = "me.tarasska"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")

    testCompileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.0")
    implementation("org.http4k:http4k-server-jetty:4.10.0.0.0")
    implementation("com.typesafe.akka:akka-actor_3:2.6.18")
    testImplementation("com.typesafe.akka:akka-testkit_3:2.6.18")
    implementation("org.http4k:http4k-core:4.13.0.0")
    implementation("com.beust:klaxon:5.5")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}