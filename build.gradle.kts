plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

version = "1.2.1-SNAPSHOT"

// Configuring main stuff for all projects.
allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    group = "dev.hawu.plugins"
    setBuildDir("../build/${project.name}")

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
        maven("https://jitpack.io")
    }

    dependencies {
        api("org.jetbrains:annotations:23.0.0")
        implementation("com.google.code.gson:gson:2.8.9")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

setBuildDir("./build/parent")
setProperty("libsDirName", project.name)

dependencies {
    implementation("org.bukkit:bukkit:1.8-R0.1-SNAPSHOT")
    subprojects.forEach {
        compileClasspath(it)
    }
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
