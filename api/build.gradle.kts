dependencies {
    implementation("org.bukkit:bukkit:1.8-R0.1-SNAPSHOT")
}

version = "1.1.3-SNAPSHOT"
val libraryName = "hikari-library"

tasks.jar {
    archiveBaseName.set(libraryName)
    archiveAppendix.set(project.name.toLowerCase())
}

val sourcesJar by tasks.creating(Jar::class) {
    from(sourceSets.main.get().allSource)
    archiveBaseName.set(libraryName)
    archiveAppendix.set(project.name.toLowerCase())
    archiveClassifier.set("sources")
}

val javadocJar by tasks.creating(Jar::class) {
    dependsOn(tasks.javadoc.get())
    from(tasks.javadoc.get())
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
