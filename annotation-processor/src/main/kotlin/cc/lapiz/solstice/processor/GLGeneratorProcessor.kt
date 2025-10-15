package cc.lapiz.solstice.processor

import com.google.auto.service.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.util.Locale.*
import javax.annotation.processing.*
import javax.lang.model.*
import javax.lang.model.element.*
import javax.lang.model.type.*

@AutoService(Processor::class) @OptIn(DelicateKotlinPoetApi::class) @SupportedAnnotationTypes("cc.lapiz.solstice.processor.GenerateGL") @SupportedSourceVersion(SourceVersion.RELEASE_21) class GLGeneratorProcessor : AbstractProcessor() {
	override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
		try {
			val annotatedElements = roundEnv.getElementsAnnotatedWith(GenerateGL::class.java)

			if (annotatedElements.isEmpty()) return false

			val fileSpec = FileSpec.builder("cc.lapiz.solstice.rendering.platform", "Graphics").addImport("cc.lapiz.solstice.rendering.platform", "Gr").addType(generateGLClass(annotatedElements)).build()

			fileSpec.writeTo(processingEnv.filer)
			return true
		} catch (e: Exception) {
			processingEnv.messager.printMessage(
				javax.tools.Diagnostic.Kind.ERROR, "Error generating `Graphics` class: ${e.message}\n${e.stackTrace.joinToString("\n")}"
			)
			return false
		}
	}


	private fun generateGLClass(elements: Set<Element>): TypeSpec {
		return TypeSpec.objectBuilder("Graphics").apply {
			elements.forEach { element ->
				when (element.simpleName.toString()) {
					"Functions" -> addFunctionDelegates(this, element)
					"Types" -> addTypeProperties(this, element)
					"Capabilities" -> addCapabilityDelegates(this, element)
				}
			}
		}.build()
	}

	private fun addFunctionDelegates(builder: TypeSpec.Builder, element: Element) {
		val functions = element.enclosedElements.filterIsInstance<ExecutableElement>().filter { it.kind == ElementKind.METHOD }

		functions.forEach { function ->
			val parameters = function.parameters.map {
				ParameterSpec.builder(it.simpleName.toString(), mapJavaTypeToKotlin(it.asType())).build()
			}

			val paramNames = parameters.joinToString(", ") { it.name }
			val returnType = mapJavaTypeToKotlin(function.returnType)

			builder.addFunction(FunSpec.builder(function.simpleName.toString()).addParameters(parameters).returns(returnType).apply {
				if (returnType == UNIT) {
					addStatement("Gr.Functions.${function.simpleName}($paramNames)")
				} else {
					addStatement("return Gr.Functions.${function.simpleName}($paramNames)")
				}
			}.build())
		}
	}

	private fun addTypeProperties(builder: TypeSpec.Builder, element: Element) {
		val properties = element.enclosedElements.filterIsInstance<ExecutableElement>().filter { it.kind == ElementKind.METHOD && it.parameters.isEmpty() }

		properties.forEach { property ->
			val propName = property.simpleName.toString().replaceFirst("get", "")
			builder.addProperty(
				PropertySpec.builder(propName, Int::class).getter(
					FunSpec.getterBuilder().addStatement("return Gr.Types.$propName").build()
				).build()
			)
		}
	}

	private fun addCapabilityDelegates(builder: TypeSpec.Builder, element: Element) {
		val properties = element.enclosedElements.filterIsInstance<ExecutableElement>().filter { it.kind == ElementKind.METHOD && it.parameters.isEmpty() }

		properties.forEach { property ->
			val propName = property.simpleName.toString().replaceFirst("get", "").replaceFirstChar { it.lowercase(getDefault()) }
			builder.addProperty(
				PropertySpec.builder(propName, property.returnType.asTypeName()).getter(
					FunSpec.getterBuilder().addStatement("return Gr.Capabilities.$propName").build()
				).build()
			)
		}
	}

	private fun mapJavaTypeToKotlin(type: TypeMirror): TypeName {
		return when (type.kind) {
			TypeKind.BOOLEAN -> BOOLEAN
			TypeKind.BYTE -> BYTE
			TypeKind.SHORT -> SHORT
			TypeKind.INT -> INT
			TypeKind.LONG -> LONG
			TypeKind.CHAR -> CHAR
			TypeKind.FLOAT -> FLOAT
			TypeKind.DOUBLE -> DOUBLE
			TypeKind.VOID -> UNIT
			TypeKind.DECLARED -> {
				val declaredType = type as DeclaredType
				val element = declaredType.asElement() as TypeElement
				when (element.qualifiedName.toString()) {
					"java.lang.String" -> STRING
					"java.nio.Buffer" -> ClassName("java.nio", "Buffer")
					"java.nio.FloatBuffer" -> ClassName("java.nio", "FloatBuffer")
					else -> type.asTypeName()
				}
			}
			TypeKind.ARRAY -> {
				val declaredType = type as ArrayType
				ClassName("kotlin", "Array").parameterizedBy(when(declaredType.componentType.asTypeName().toString()) {
					"java.lang.Byte" -> BYTE
					"java.lang.Short" -> SHORT
					"java.lang.Int" -> INT
					"java.lang.Long" -> LONG
					"java.lang.Char" -> CHAR
					"java.lang.Float" -> FLOAT
					"java.lang.Double" -> DOUBLE
					"java.lang.Boolean" -> BOOLEAN
					else -> declaredType.componentType.asTypeName()
				})
			}

			else -> type.asTypeName()
		}
	}
}