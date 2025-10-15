plugins {
	id("java")
	id("rtsg-global")
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
	api(libs.bundles.logging)
	api(platform(libs.lwjgl.bom))
	api(libs.bundles.lwjgl)
	api(libs.bundles.imgui)
	api(libs.joml)
	libs.bundles.lwjgl.get().forEach {
		runtimeOnly(it.group, it.name, classifier = LWJGL.platform)
	}
    implementation(kotlin("reflect"))
}

kotlin {
	jvmToolchain(21)
}