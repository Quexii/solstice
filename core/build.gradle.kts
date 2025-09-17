plugins {
	id("java")
	id("rtsg-global")
	kotlin("kapt")
	alias(libs.plugins.kotlin)
	alias(libs.plugins.serialization)
}

group = "cc.lapiz"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	implementation(libs.bundles.kotlin)
	kapt(project(":annotation-processor"))
	compileOnly(project(":annotation-processor"))
	api(libs.bundles.logging)
	api(platform(libs.lwjgl.bom))
	api(libs.bundles.lwjgl.shared)
	api("org.joml", "joml", "1.10.8")
	libs.bundles.lwjgl.shared.get().forEach {
		runtimeOnly(it.group, it.name, classifier = LWJGL.platform)
	}
}

kotlin {
	jvmToolchain(21)
}