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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClassName = "Main"
}

javafx {
    version = "14"
    modules = listOf("javafx.controls")
}
