plugins {
    java
    `maven-publish`
}

// Configuring main stuff for all projects.
allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

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

val default = "1.0-SNAPSHOT"

// Versioning
project(":collections") {
    version = "1.3-SNAPSHOT"
}

project(":core") {
    version = "1.3-SNAPSHOT"
}

project(":commands") {
    version = "1.1.2-SNAPSHOT"
}

project(":events") {
    version = "1.0.1-SNAPSHOT"
}

project(":reflect") {
    version = "1.2-SNAPSHOT"
}

project(":particles") {
    version = "1.0-SNAPSHOT"
}

project(":math") {
    version = "1.0-SNAPSHOT"
}

project(":nbt") {
    version = "2.0.3-SNAPSHOT"
}

project(":holograms") {
    version = "0.1-SNAPSHOT"
}

project(":inventories") {
    version = "2.4.1-SNAPSHOT"
}

project(":v1_8_R1") {
    version = default
}

project(":v1_8_R2") {
    version = default
}

project(":v1_8_R3") {
    version = default
}

project(":v1_9_R1") {
    version = default
}

project(":v1_9_R2") {
    version = default
}

project(":v1_10_R1") {
    version = default
}

project(":v1_11_R1") {
    version = default
}

project(":v1_12_R1") {
    version = default
}

project(":v1_13_R1") {
    version = default
}

project(":v1_13_R2") {
    version = default
}

project(":v1_14_R1") {
    version = default
}

project(":v1_15_R1") {
    version = default
}

project(":v1_16_R1") {
    version = default
}

project(":v1_16_R2") {
    version = default
}

project(":v1_16_R3") {
    version = default
}

project(":v1_17_R1") {
    version = default
}


// Configuring publishing for subprojects.
subprojects {
    val libraryPrefix = "hikari-library"

    tasks.jar {
        archiveBaseName.set(libraryPrefix)
        archiveAppendix.set(project.name.toLowerCase())
    }

    val sourcesJar by tasks.creating(Jar::class) {
        from(sourceSets.main.get().allSource)
        archiveBaseName.set(libraryPrefix)
        archiveAppendix.set(project.name.toLowerCase())
        archiveClassifier.set("sources")
    }

    val javadocJar by tasks.creating(Jar::class) {
        dependsOn(tasks.javadoc.get())
        from(tasks.javadoc.get())
        archiveBaseName.set(libraryPrefix)
        archiveAppendix.set(project.name.toLowerCase())
        archiveClassifier.set("javadoc")
    }

    publishing {
        publications {
            create<MavenPublication>(project.name) {
                groupId = "dev.hawu.plugins"
                artifactId = "$libraryPrefix-${project.name.toLowerCase()}"
                version = project.version.toString()
                pom {
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0")
                        }
                    }
                }
                from(components["java"])
                artifact(sourcesJar)
                artifact(javadocJar)
            }
        }
    }
}
