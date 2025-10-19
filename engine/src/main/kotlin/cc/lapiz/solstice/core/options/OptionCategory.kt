package cc.lapiz.solstice.core.options

class OptionCategory(
    val name: String,
    val description: String = ""
) {
    private val options = mutableMapOf<String, Option<*>>()

    fun <T> addOption(option: Option<T>): Option<T> {
        options[option.key] = option
        return option
    }

    fun getOption(key: String): Option<*>? = options[key]

    fun getAllOptions(): Map<String, Option<*>> = options.toMap()

    fun removeOption(key: String): Option<*>? = options.remove(key)
}