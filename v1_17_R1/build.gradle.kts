java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

dependencies {
    implementation(files("../libs/craftbukkit-1.17.jar"))
    implementation(project(":nbt"))
    implementation(project(":titles"))
    implementation(project(":particles"))
    implementation(project(":reflect"))
}
