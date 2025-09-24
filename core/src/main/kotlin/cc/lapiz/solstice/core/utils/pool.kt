package cc.lapiz.solstice.core.utils

class SimplePool<T>(private val init: () -> T, private val reset: (T) -> Unit) {
	private val stack = ArrayDeque<T>()
	fun obtain(): T = if (stack.isEmpty()) init() else stack.removeLast()
	fun free(obj: T) {
		reset(obj)
		stack.addLast(obj)
	}
	fun size(): Int = stack.size
}