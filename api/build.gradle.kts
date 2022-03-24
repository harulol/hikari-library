plugins {
    id("org.jetbrains.dokka")
    kotlin("jvm")
}

dependencies {
    implementation("org.bukkit:bukkit:1.8-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
}

val libraryName = "hikari-library"

tasks.jar {
    archiveBaseName.set(libraryName)
    archiveAppendix.set(project.name.toLowerCase())
}

tasks.dokkaJavadoc {
    outputDirectory.set(buildDir.resolve("dokka"))
    moduleName.set("HikariLibrary")

    dokkaSourceSets {
        named("main") {
            reportUndocumented.set(true)
            skipEmptyPackages.set(true)
            displayName.set("HikariLibrary")
            println(file(".").absolutePath)
            platform.set(org.jetbrains.dokka.Platform.jvm)
            sourceRoots.from(file("src"))
            jdkVersion.set(8)
        }
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    from(sourceSets.main.get().allSource)
    archiveBaseName.set(libraryName)
    archiveAppendix.set(project.name.toLowerCase())
    archiveClassifier.set("sources")
}

val javadocJar by tasks.creating(Jar::class) {
    dependsOn(tasks.dokkaJavadoc.get())
    from(tasks.dokkaJavadoc.get())
    archiveBaseName.set(libraryName)
    archiveAppendix.set(project.name.toLowerCase())
    archiveClassifier.set("javadoc")
}

publishing {
    repositories {
        maven("https://maven.pkg.github.com/harulol/hikari-library") {
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>(project.name) {
            groupId = "dev.hawu.plugins"
            artifactId = libraryName
            version = rootProject.version.toString()
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
