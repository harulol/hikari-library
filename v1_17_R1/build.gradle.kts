java {
    toolchain {
        JavaLanguageVersion.of(16)
    }
}

dependencies {
    implementation(files("../libs/craftbukkit-1.17.jar"))
    implementation(project(":api"))
}
