plugins {
	application
	id("org.openjfx.javafxplugin") version "0.0.8"
	id("com.github.johnrengelman.shadow") version "5.2.0"
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
	mainClassName = "pl.pitcer.ive.Ive"
}

javafx {
	version = "11"
	modules = listOf("javafx.controls")
}
