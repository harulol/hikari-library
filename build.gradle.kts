plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

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
        implementation("org.jetbrains:annotations:22.0.0")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

setBuildDir("./build/parent")

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
