@file:OptIn(InternalSerializationApi::class)

package cc.lapiz.solstice.core.dev

import cc.lapiz.solstice.core.data.*
import cc.lapiz.solstice.core.event.*
import cc.lapiz.solstice.core.font.FontManager
import cc.lapiz.solstice.core.game.*
import cc.lapiz.solstice.core.game.ecs.component.impl.*
import cc.lapiz.solstice.core.game.ecs.component.internal.*
import cc.lapiz.solstice.core.game.ecs.entity.*
import cc.lapiz.solstice.core.resource.*
import cc.lapiz.solstice.core.resource.impl.*
import cc.lapiz.solstice.core.ui.immediate.*
import cc.lapiz.solstice.core.window.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

private typealias ui = SIUI
private typealias CompTarget = cc.lapiz.solstice.core.data.Target

object Editor {
	// scene data
	private var scene: Scene? = null
	private var selectedArchetype: Archetype? = null
	private var selectedEntity: Entity? = null

	// resources data
	private var selectedResource: Resource? = null

	private var currentMode: EditorMode = EditorMode.SCENE

	private val json = Json {
		prettyPrint = true
		isLenient = true
		ignoreUnknownKeys = true
	}

	init {
		registerCustomRenderers()
	}

	private fun registerCustomRenderers() {
		ui.registerPropertyRenderer(Vector2::class.simpleName!!) { name, value ->
			val v = value as Vector2
			ui.labeledRow(name, "x" to v.x, "y" to v.y)
		}

		ui.registerPropertyRenderer(Vector3::class.simpleName!!) { name, value ->
			val v = value as Vector3
			ui.labeledRow(name, "x" to v.x, "y" to v.y, "z" to v.z)
		}

		ui.registerPropertyRenderer("Vector4") { name, value ->
			val v = value as Vector4
			ui.labeledRow(name, "x" to v.x, "y" to v.y, "z" to v.z, "w" to v.w)
		}

		ui.registerPropertyRenderer(Color::class.simpleName!!) { name, value ->
			val c = value as Color
			ui.labeledRow(name, "r" to c.r, "g" to c.g, "b" to c.b, "a" to c.a)
		}

		ui.registerPropertyRenderer(CompTarget::class.simpleName!!) { name, value ->
			val t = value as CompTarget
			when (t.type) {
				TargetType.POSITION -> ui.labeledRow(name, "Position" to "(${t.x}, ${t.y})")
				TargetType.ENTITY -> ui.labeledRow(name, "Entity" to "#")
				TargetType.NONE -> ui.labeledRow(name, "None" to "")
			}

			ui.label("Type: ${t.type}")
			if (t.path != null && t.path!!.isNotEmpty()) {
				ui.label("Path:")
				ui.indent()
				for (point in t.path) {
					ui.label(" - (${point.x}, ${point.y})")
				}
				ui.unindent()
			} else {
				ui.label("Path: (none)")
			}
			ui.label("Navigating: ${t.navigating}")
		}
	}

	fun update(delta: Float) {
		scene = SceneManager.getCurrent()
	}

	fun render() {}

	fun nanovg() {
		ui.beginFrame()
		when (currentMode) {
			EditorMode.SCENE -> displayScene()
			EditorMode.PREFABS -> {
				ui.beginWindow("Prefabs", 0f, 0f, Window.width().toFloat(), Window.height().toFloat())
				ui.label("Prefabs Editor - Work in Progress")
				ui.endWindow()
			}
			EditorMode.RESOURCES -> displayResources()
		}
		ui.beginWindow("Tools", 0f, Window.height() - 54f, Window.width().toFloat(), 54f, false)
		ui.horizontal()
		if (ui.imageButton(ResourceManager.get($$"$scene")?:error("Missing resource for Editor Icon"), 32f, 32f, 26f, 26f)) {
			currentMode = EditorMode.SCENE
		}
		if (ui.imageButton(ResourceManager.get($$"$prefabs")?:error("Missing resource for Editor Icon"), 32f, 32f, 26f, 26f)) {
			currentMode = EditorMode.PREFABS
		}
		if (ui.imageButton(ResourceManager.get($$"$resources")?:error("Missing resource for Editor Icon"), 32f, 32f, 26f, 26f)) {
			currentMode = EditorMode.RESOURCES
		}
		ui.vertical()
		ui.endWindow()
		ui.endFrame()
	}

	private fun displayScene() {
		ui.beginWindow("Scene Explorer", 0f, 0f, 350f, Window.height().toFloat())
		if (scene != null) {
			if (selectedArchetype != null) {
				run archetype@{
					ui.label("Archetype: " + selectedArchetype!!.signature)

					if (ui.button("Back")) {
						selectedArchetype = null
						selectedEntity = null
						return@archetype
					}

					ui.label(selectedArchetype!!.entities.size.toString() + " entities")
					for (entity in selectedArchetype!!.entities) {
						if (ui.button("Entity #$entity")) {
							selectedEntity = entity
						}
					}
					if (selectedEntity != null) {
						run entity@{
							ui.beginWindow("Entity #" + selectedEntity!!, Window.width().toFloat() - 350f, 0f, 350f, Window.height().toFloat())
							if (ui.button("Deselect")) {
								selectedEntity = null
								return@entity
							}
							if (ui.button("Serialize")) {
								serializeEntity(selectedEntity!!, selectedArchetype!!)
							}

							for (comp in selectedArchetype!!.getComponents(selectedEntity!!)!!) {
								val componentName = comp.key.simpleName ?: comp.key.toString()
								val componentId = "component_${selectedEntity}_${componentName}"

								if (ui.collapsingHeader(componentName, componentId)) {
									ui.indent()
									displayComponentProperties(comp.value)
									ui.unindent()
								}
							}
							ui.endWindow()
						}
					}
				}
			} else {
				ui.label(scene!!.javaClass.simpleName + " (" + scene!!.ecs.archtypes().size + "a)")
				for (type in scene!!.ecs.archtypes()) {
					if (ui.button(type.signature.toString())) {
						selectedArchetype = type
						selectedEntity = null
					}
				}
			}
		}
		ui.endWindow()
	}

	private fun displayComponentProperties(component: Any) {
		if (component is SpriteRenderer) {
			ui.label("  spriteId: \"${component.spriteId}\"")

			ui.propertyValue("  color", component.color)

			if (component.spriteId.isNotEmpty()) {
				val spriteResource = ResourceManager.get<SpriteResource>(component.spriteId)
				if (spriteResource != null) {
					ui.spacing(4f)
					ui.label("  Preview:")
					ui.image(spriteResource, 64f, 64f)
				}
			}
			return
		}

		try {
			val serializer = component::class.serializer()
			@Suppress("UNCHECKED_CAST")
			val jsonString = json.encodeToString(serializer as KSerializer<Any>, component)
			val jsonElement = json.parseToJsonElement(jsonString)

			if (jsonElement is JsonObject) {
				for ((key, value) in jsonElement) {
					displayJsonValue(key, value, component, indent = "  ")
				}
			} else {
				ui.label("  $jsonElement")
			}
		} catch (_: Exception) {
			ui.label("  (not serializable - using reflection)")
			displayComponentPropertiesReflection(component)
		}
	}

	private fun displayJsonValue(key: String, value: JsonElement, component: Any, indent: String = "") {
		when (value) {
			is JsonPrimitive -> {
				when {
					value.isString -> ui.label("$indent$key: \"${value.content}\"")
					else -> ui.label("$indent$key: ${value.content}")
				}
			}
			is JsonObject -> {
				val actualValue = try {
					val field = component.javaClass.getDeclaredField(key)
					field.isAccessible = true
					field.get(component)
				} catch (_: Exception) {
					null
				}

				if (actualValue != null) {
					ui.propertyValue("$indent$key", actualValue)
				} else {
					ui.label("$indent$key: {")
					for ((nestedKey, nestedValue) in value) {
						displayJsonValue(nestedKey, nestedValue, component, "$indent  ")
					}
					ui.label("$indent}")
				}
			}
			is JsonArray -> {
				ui.label("$indent$key: [${value.size} items]")
			}
		}
	}

	private fun displayComponentPropertiesReflection(component: Any) {
		val javaClass = component.javaClass
		val fields = javaClass.declaredFields

		if (fields.isEmpty()) {
			ui.label("    (no properties)")
			return
		}

		for (field in fields) {
			try {
				field.isAccessible = true

				if (java.lang.reflect.Modifier.isTransient(field.modifiers)) {
					continue
				}

				val value = field.get(component)
				val fieldName = field.name

				when (value) {
					is Float -> ui.label("    $fieldName: %.3f".format(value))
					is Double -> ui.label("    $fieldName: %.3f".format(value))
					is Int -> ui.label("    $fieldName: $value")
					is Long -> ui.label("    $fieldName: $value")
					is Boolean -> ui.label("    $fieldName: $value")
					is String -> ui.label("    $fieldName: \"$value\"")
					is Char -> ui.label("    $fieldName: '$value'")
					is Byte -> ui.label("    $fieldName: $value")
					is Short -> ui.label("    $fieldName: $value")
					null -> ui.label("    $fieldName: null")
					else -> {
						ui.propertyValue("    $fieldName", value)
					}
				}
			} catch (_: Exception) {
				ui.label("    ${field.name}: <error>")
			}
		}
	}

	private fun serializeEntity(entity: Entity, archetype: Archetype) {
		try {
			val components = archetype.getComponents(entity) ?: emptyMap()

			val jsonObject = buildJsonObject {
				putJsonObject("components") {
					for ((componentClass, component) in components) {
						val componentName = componentClass.simpleName ?: componentClass.toString()

						if (component is cc.lapiz.solstice.core.game.ecs.component.impl.Children) {
							putJsonArray("Children") {
								for (childId in component.entities) {
									val childArchetype = scene?.ecs?.archtypes()?.find {
										it.entities.contains(childId)
									}
									if (childArchetype != null) {
										add(serializeEntityToJson(childId, childArchetype))
									}
								}
							}
							continue
						}

						try {
							val serializer = component::class.serializer()
							@Suppress("UNCHECKED_CAST")
							val componentJson = json.encodeToJsonElement(serializer as KSerializer<Any>, component)
							put(componentName, componentJson)
						} catch (e: Exception) {
							put(componentName, buildJsonObject {
								put("error", "Serialization failed: ${e.message}")
							})
						}
					}
				}
			}

			val jsonString = json.encodeToString(JsonObject.serializer(), jsonObject)
			println("\n=== Entity Serialization ===")
			println(jsonString)
			println("============================\n")

		} catch (e: Exception) {
			println("Failed to serialize entity: ${e.message}")
			e.printStackTrace()
		}
	}

	private fun serializeEntityToJson(entity: Entity, archetype: Archetype): JsonObject {
		val components = archetype.getComponents(entity) ?: emptyMap()

		return buildJsonObject {
			putJsonObject("components") {
				for ((componentClass, component) in components) {
					val componentName = componentClass.simpleName ?: componentClass.toString()

					if (component is cc.lapiz.solstice.core.game.ecs.component.impl.Children) {
						putJsonArray("Children") {
							for (childId in component.entities) {
								val childArchetype = scene?.ecs?.archtypes()?.find {
									it.entities.contains(childId)
								}
								if (childArchetype != null) {
									add(serializeEntityToJson(childId, childArchetype))
								}
							}
						}
						continue
					}

					try {
						val serializer = component::class.serializer()
						@Suppress("UNCHECKED_CAST")
						val componentJson = json.encodeToJsonElement(serializer as KSerializer<Any>, component)
						put(componentName, componentJson)
					} catch (e: Exception) {
						put(componentName, buildJsonObject {
							put("error", "Serialization failed: ${e.message}")
						})
					}
				}
			}
		}
	}

	private fun displayResources() {
		ui.beginWindow("Resource Explorer", 8f, Window.height() - 54 - 210f, Window.width().toFloat() - 16f, 200f)
		val allResources = ResourceManager.getAll()
		val itemSize = 64f
		val padding = 8f
		val columns = (((Window.width() - padding - 12) / (itemSize + padding)).toInt() - 1).coerceAtLeast(1)
		ui.horizontal()
		var index = 0
		allResources.forEach{ resource ->
			val image = when (resource) {
				is FontResource -> ResourceManager.get($$"$font")
				is ShaderResource -> ResourceManager.get($$"$shader")
				is SpriteResource -> ResourceManager.get($$"$image")
				else -> ResourceManager.get<SpriteResource>($$"$file")
			}
			val clicked = if (image != null) {
				ui.imageButton(image, itemSize, itemSize, itemSize, itemSize)
			} else {
				ui.button(resource.name, itemSize, itemSize)
			}

			if (clicked) {
				selectedResource = resource
			}

			if (index >= columns) {
				ui.newLine(padding + itemSize)
				ui.spacing(12f)
				index = 0
			}
			index++
		}
		ui.vertical()
		ui.endWindow()

		if (selectedResource != null) {
			ui.beginWindow("Resource: " + selectedResource!!.name, 8f, 8f, Window.width().toFloat() - 16f, Window.height() - 58 - 220f)
			run resource@{
				if (ui.button("Deselect")) {
					selectedResource = null
					return@resource
				}
				ui.label("ID: " + selectedResource!!.id)
				ui.label("Name: " + selectedResource!!.name)
				ui.label("Type: " + selectedResource!!::class.simpleName)
				when (selectedResource) {
					is SpriteResource -> {
						val res = selectedResource as SpriteResource
						ui.label("Path: " + res.path)
						ui.label("Dimensions: ${res.width()} x ${res.height()}")
						ui.label("Channels: ${res.channels()}")
						ui.spacing(4f)
						ui.label("Preview:")
						ui.image(res, 128f, 128f)
					}
					is FontResource -> {
						val res = selectedResource as FontResource
						ui.label("Path: " + res.path)
						for (font in res.layout.fonts) {
							ui.setStyle(ui.getStyle().copy(
								font = FontManager.getFont(res.id)!!.face(font.face)!!
							))
							ui.label(font.face)
							ui.label("The quick brown fox jumps over the lazy dog")
						}
						ui.setStyle(ui.getStyle().copy(
							font = FontManager.Default.Default
						))
					}
					is ShaderResource -> {
						val res = selectedResource as ShaderResource
						val layout = res.layout
						ui.label("Path: " + res.path)
						ui.label("Name: " + layout.name)
						for (shader in layout.shaders) {
							ui.label("  ${shader.type.uppercase()}: ${shader.file}")
						}
						for (attribute in layout.attributes) {
							ui.label("  Attribute: ${attribute.name} (${attribute.type})")
						}
						for (uniform in layout.uniforms) {
							ui.label("  Uniform: ${uniform.name} (${uniform.type})")
						}
					}
				}
			}
			ui.endWindow()
		}
	}

	enum class EditorMode {
		SCENE,
		PREFABS,
		RESOURCES
	}
}