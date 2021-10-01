java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

dependencies {
    implementation(project(":nbt"))
    implementation(fileTree("../libs/"))
}
