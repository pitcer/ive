plugins {
    application
    id("org.openjfx.javafxplugin") version "0.0.8"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

repositories {
    jcenter()
}

dependencies {
    compileOnly("org.jetbrains:annotations:19.0.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClassName = "Main"
}

javafx {
    version = "14"
    modules = listOf("javafx.controls")
}
