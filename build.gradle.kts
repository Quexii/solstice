
plugins {
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
}

kotlin {
	compilerOptions {
		freeCompilerArgs.add("-Xwhen-expressions=indy")
	}
}