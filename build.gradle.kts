import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `maven-publish`
    id("org.jetbrains.dokka") version "1.7.20"
    kotlin("jvm") version "1.8.10"
    id("com.github.johnrengelman.shadow") version "8.1.0"
    scala
}

version = "2.1-SNAPSHOT"

// Configuring main stuff for all projects.
allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "kotlin")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.named<KotlinCompile>("compileKotlin") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    group = "dev.hawu.plugins"
    setBuildDir("../build/${project.name}")

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
        maven("https://jitpack.io")
    }

    dependencies {
        api("org.jetbrains:annotations:23.1.0")
        api("org.jetbrains.kotlin:kotlin-stdlib:1.8.0")
        api("com.google.code.gson:gson:2.10")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

setBuildDir("./build/parent")
setProperty("libsDirName", project.name)

dependencies {
    api("org.bukkit:bukkit:1.8-R0.1-SNAPSHOT")
    subprojects.forEach {
        @Suppress("DEPRECATION")
        implementation(it)
    }
    testImplementation(project(":api"))
}

tasks.shadowJar {
    configurations = listOf(project.configurations.getByName("compileClasspath"))
    dependencies {
        subprojects.forEach {
            include(dependency(it))
        }
    }
    archiveFileName.set("HikariLibrary-${project.version}.jar")
    tasks.jar.get().finalizedBy(this)
    isZip64 = true
}
