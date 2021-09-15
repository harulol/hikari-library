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
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    }

    tasks.test {
        useJUnitPlatform()
    }
}


setBuildDir("./build/parent")

// Versioning
project(":core") {
    version = "1.1-SNAPSHOT"
}

project(":commands") {
    version = "1.0-SNAPSHOT"
}

project(":events") {
    version = "1.0.1-SNAPSHOT"
}

project(":reflect") {
    version = "1.1-SNAPSHOT"
}

project(":nbt") {
    version = "1.1.1-RC"
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
        repositories {
            maven("https://maven.pkg.github.com/harulol/hikari-library") {
                name = "GithubPackages"
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }

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
