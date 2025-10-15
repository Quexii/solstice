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
	implementation(project(":engine"))
}

kotlin {
	jvmToolchain(21)
}

application {
	mainClass.set("cc.lapiz.solstice.game.MainKt")
}

tasks.named<JavaExec>("run") {
	this.workingDir = File("run").apply { mkdir() }

	jvmArgs = listOf("-Dsolstice.editor=true")
	if (System.getenv("XDG_SESSION_TYPE") == "wayland") {
        environment("XDG_SESSION_TYPE", "x11")
        environment("WAYLAND_DISPLAY", "")
        environment("DISPLAY", ":0")
    }
}
