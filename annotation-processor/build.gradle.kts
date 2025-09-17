plugins {
	kotlin("jvm")
	kotlin("kapt")
}

group = "cc.lapiz"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.google.auto.service:auto-service:1.0.1")
	kapt("com.google.auto.service:auto-service:1.0.1")
	implementation("com.squareup:kotlinpoet:1.14.2")
}

kotlin {
	jvmToolchain(21)
}