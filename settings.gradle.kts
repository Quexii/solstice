plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "solstice"

include("engine")
include("game")
include("annotation-processor")