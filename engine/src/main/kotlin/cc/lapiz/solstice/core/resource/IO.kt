package cc.lapiz.solstice.resource

import org.lwjgl.*
import org.lwjgl.system.*
import java.net.*
import java.nio.*
import java.nio.channels.*
import java.nio.file.*

/**
 * Utility object for loading resources from various locations (local file system or URL).
 * Based on LWJGL3's IOUtil.
 */
object IO {
	fun getText(resource: String): String {
		val path: Path? = if (resource.startsWith("http")) null else Paths.get(resource)
		return if (path != null && Files.isReadable(path)) {
			Files.readString(path)
		} else {
			(if (resource.startsWith("http")) URI.create(resource).toURL().openStream()
			else IO::class.java.getResourceAsStream("/assets/$resource")).bufferedReader().use { it.readText() }
		}
	}

	fun getBuffer(resource: String, bufferSize: Int = 512): ByteBuffer {
		var buffer: ByteBuffer

		val path: Path? = if (resource.startsWith("http")) null else Paths.get(resource)
		if (path != null && Files.isReadable(path)) {
			Files.newByteChannel(path).use { fc ->
				buffer = BufferUtils.createByteBuffer(fc.size().toInt() + 1)
				while (fc.read(buffer) != -1) {
					break
				}
			}
		} else {
			(if (resource.startsWith("http")) URI.create(resource).toURL().openStream()
			else IO::class.java.getResourceAsStream("/assets/$resource")).use { source ->
				Channels.newChannel(source).use { rbc ->
					buffer = BufferUtils.createByteBuffer(bufferSize)
					while (true) {
						val bytes: Int = rbc.read(buffer)
						if (bytes == -1) {
							break
						}
						if (buffer.remaining() == 0) {
							buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2) // 50%
						}
					}
				}
			}
		}

		buffer.flip()
		return MemoryUtil.memSlice(buffer)
	}

	private fun resizeBuffer(buffer: ByteBuffer, newCapacity: Int): ByteBuffer {
		val newBuffer = BufferUtils.createByteBuffer(newCapacity)
		buffer.flip()
		newBuffer.put(buffer)
		return newBuffer
	}
}