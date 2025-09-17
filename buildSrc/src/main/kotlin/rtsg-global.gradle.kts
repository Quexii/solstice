import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.JavaExec

tasks.withType<JavaExec> {
	jvmArgs("--enable-native-access=ALL-UNNAMED")
}

tasks.withType<Test> {
	jvmArgs("--enable-native-access=ALL-UNNAMED")
}

tasks.withType<JavaCompile> {
	options.compilerArgs.add("--enable-native-access=ALL-UNNAMED")
}
