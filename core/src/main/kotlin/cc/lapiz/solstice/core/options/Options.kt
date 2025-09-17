package cc.lapiz.solstice.core.options

import kotlinx.serialization.json.*
import java.io.*
import java.util.concurrent.*

/**
 * Main options manager that handles categories and option values
 */
object Options {
	private val categories = mutableMapOf<String, OptionCategory>()
	private val values = ConcurrentHashMap<String, Any>()
	private val json = Json {
		prettyPrint = true
		ignoreUnknownKeys = true
	}

	fun category(name: String, description: String = ""): OptionCategory {
		return categories.getOrPut(name) { OptionCategory(name, description) }
	}

	fun getCategory(name: String): OptionCategory? = categories[name]

	fun getAllCategories(): Map<String, OptionCategory> = categories.toMap()

	object Categories {
		val GENERAL = category("General", "General application settings")
		val GRAPHICS = category("Graphics", "Graphics and display settings")
		val AUDIO = category("Audio", "Audio and sound settings")
		val CONTROLS = category("Controls", "Input and control settings")
	}

	val VSYNC = Categories.GRAPHICS.addOption(BooleanOption("VSYNC", false))

	fun <T> set(option: Option<T>, value: T): Boolean {
		return if (option.validate(value)) {
			values[option.key] = value as Any
			true
		} else false
	}

	@Suppress("UNCHECKED_CAST")
	fun <T> get(option: Option<T>): T {
		return values[option.key] as? T ?: option.defaultValue
	}

	fun setFromString(option: Option<*>, value: String): Boolean {
		return when (val parsed = option.parse(value)) {
			null -> false
			else -> {
				@Suppress("UNCHECKED_CAST") set(option as Option<Any>, parsed)
			}
		}
	}

	fun reset(option: Option<*>) {
		values.remove(option.key)
	}

	fun resetAll() {
		values.clear()
	}

	fun findOption(key: String): Option<*>? {
		return categories.values.firstNotNullOfOrNull { it.getOption(key) }
	}

	fun saveToFile(file: File) {
		val optionValues = values.map { (key, value) ->
			val option = findOption(key)
			if (option != null) {
				@Suppress("UNCHECKED_CAST") val serializedValue = when (value) {
					is String -> value
					is Int -> value.toString()
					is Boolean -> value.toString()
					is Double -> value.toString()
					else -> value.toString()
				}
				OptionValue(key, serializedValue)
			} else null
		}.filterNotNull()

		val data = OptionsData(optionValues)
		file.writeText(json.encodeToString(data))
	}

	fun loadFromFile(file: File) {
		if (!file.exists()) return

		try {
			val data = json.decodeFromString<OptionsData>(file.readText())
			data.values.forEach { optionValue ->
				findOption(optionValue.key)?.let { option ->
					setFromString(option, optionValue.value)
				}
			}
		} catch (e: Exception) {
			println("Failed to load options: ${e.message}")
		}
	}

	fun toJson(): String {
		val optionValues = values.map { (key, value) ->
			OptionValue(key, value.toString())
		}
		return json.encodeToString(OptionsData(optionValues))
	}

	fun fromJson(jsonString: String) {
		try {
			val data = json.decodeFromString<OptionsData>(jsonString)
			data.values.forEach { optionValue ->
				findOption(optionValue.key)?.let { option ->
					setFromString(option, optionValue.value)
				}
			}
		} catch (e: Exception) {
			println("Failed to import options: ${e.message}")
		}
	}
}
