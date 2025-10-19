package cc.lapiz.solstice.core.game.components.impl

import cc.lapiz.solstice.core.data.Transform
import cc.lapiz.solstice.core.game.components.ImSerialize
import cc.lapiz.solstice.core.data.Vector2
import cc.lapiz.solstice.core.game.components.Component
import cc.lapiz.solstice.core.game.components.meta.ComponentName
import cc.lapiz.solstice.core.game.components.meta.JsonSerializable
import cc.lapiz.solstice.core.ui.imgui.table2Column
import cc.lapiz.solstice.core.ui.imgui.tableRow
import imgui.ImGui
import imgui.flag.ImGuiTableFlags
import imgui.type.ImFloat
import kotlinx.serialization.json.*

private typealias Backing = Transform

@ComponentName("Transform")
class Transform : Component(), ImSerialize, JsonSerializable {
    val backing = Backing()

    var position: Vector2
        get() = backing.position
        set(value) {
            backing.position.set(value)
            positionArray[0] = value.x
            positionArray[1] = value.y
        }

    var rotation: Float
        get() = backing.rotation
        set(value) {
            backing.rotation = value
            imRotation.data[0] = value
        }

    var scale: Vector2
        get() = backing.scale
        set(value) {
            backing.scale.set(value)
            scaleArray[0] = value.x
            scaleArray[1] = value.y
        }

    var z: Float
        get() = backing.z
        set(value) {
            backing.z = value
            imZ.data[0] = value
        }

    private val positionArray = floatArrayOf(position.x, position.y)
    private val scaleArray = floatArrayOf(scale.x, scale.y)
    private val imRotation = ImFloat(rotation)
    private val imZ = ImFloat(z)
    override fun drawImGui() {
        table2Column("##Transform") {
            tableRow("Position", positionArray)
            tableRow("Scale", scaleArray)
            tableRow("Rotation", imRotation)
            tableRow("Z", imZ)
        }
    }

    override fun toJson(): JsonObject = buildJsonObject {
        put("type", name)
        put("position", buildJsonArray {
            add(position.x)
            add(position.y)
        })
        put("rotation", rotation)
        put("scale", buildJsonArray {
            add(scale.x)
            add(scale.y)
        })
        put("z", z)
    }
}