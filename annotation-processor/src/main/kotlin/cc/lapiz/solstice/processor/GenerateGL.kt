package cc.lapiz.solstice.processor

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GenerateGL(val className: String = "Graphics")