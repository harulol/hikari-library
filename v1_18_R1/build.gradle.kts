java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    mavenLocal()
}

dependencies {
    implementation("org.spigotmc:spigot:1.18-R0.1-SNAPSHOT")
    implementation(project(":api"))
}
