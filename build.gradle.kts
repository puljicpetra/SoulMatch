import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization") version "1.9.22"
}

group = "hr.unipu.java"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

// AŽURIRANO: Dodane su ovisnosti za Material Design komponente i ikone
dependencies {
    // Osnovna ovisnost za Compose Desktop
    implementation(compose.desktop.currentOs)

    // Ovisnost za Kotlinx Serialization (za rad s JSON-om)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // NOVO: Ovisnost za Material Design komponente (Button, Card, Scaffold, itd.)
    // Vjerojatno je ovo uzrok mnogih problema, ne samo ikona.
    implementation(compose.material)

    // NOVO: Ovisnost za prošireni set Material ikona (Forum, People, itd.)
    implementation(compose.materialIconsExtended)
}

compose.desktop {
    application {
        mainClass = "hr.unipu.java.soulmatch.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "SoulMatch"
            packageVersion = "1.0.0"
        }
    }
}