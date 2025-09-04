plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "com.bindglam.enchant"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.nightexpressdev.com/releases")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("su.nightexpress.excellentenchants:Core:5.2.1") {
        exclude("org.spigotmc")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    runServer {
        minecraftVersion("1.21.4")

        downloadPlugins {
            modrinth("excellentenchants", "5.2.1")
            modrinth("nightcore", "2.7.15")
            modrinth("packetevents", "2.9.5+spigot")
        }
    }
}