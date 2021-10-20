java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(files("../libs/craftbukkit-1.17.jar"))
    implementation(project(":api"))
}
