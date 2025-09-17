import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.kotlin.com.google.common.hash.Hashing
import java.nio.charset.StandardCharsets
import java.util.Base64

plugins {
	id("java")
	id("application")
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
	implementation(project(":core"))
	implementation(libs.bundles.lwjgl.desktop)
	libs.bundles.lwjgl.desktop.get().forEach {
		runtimeOnly(it.group, it.name, classifier = LWJGL.platform)
	}
}

kotlin {
	jvmToolchain(21)
}

application {
	mainClass.set("cc.lapiz.solstice.desktop.MainKt")
}
