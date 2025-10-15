package cc.lapiz.solstice.game.components.impl

import cc.lapiz.solstice.core.game.components.ImSerialize
import cc.lapiz.solstice.data.Transform
import cc.lapiz.solstice.data.Vector2
import cc.lapiz.solstice.game.components.Component
import cc.lapiz.solstice.ui.imgui.ImGui
import imgui.flag.ImGuiTableFlags

private typealias Backing = Transform

class Transform : Component(), ImSerialize {
    val backing = Backing()

    var position: Vector2
        get() = backing.position
        set(value) {
            backing.position.set(value)
            positionArrayX[0] = value.x
            positionArrayY[0] = value.y
        }

    var rotation: Float
        get() = backing.rotation
        set(value) {
            backing.rotation = value
            rotationArray[0] = value
        }

    var scale: Vector2
        get() = backing.scale
        set(value) {
            backing.scale.set(value)
            scaleArrayX[0] = value.x
            scaleArrayY[0] = value.y
        }

    var z: Float
        get() = backing.z
        set(value) {
            backing.z = value
            zArray[0] = value
        }

    private val positionArrayX = floatArrayOf(position.x)
    private val positionArrayY = floatArrayOf(position.y)
    private val scaleArrayX = floatArrayOf(scale.x)
    private val scaleArrayY = floatArrayOf(scale.y)
    private val rotationArray = floatArrayOf(rotation)
    private val zArray = floatArrayOf(z)
    override fun drawImGui() {
        ImGui.beginTable("##transform", 3, ImGuiTableFlags.SizingFixedFit)
        val size = imgui.ImGui.getContentRegionAvail()

        ImGui.tableNextRow()
        val fieldWidth = (size.x + 24) / 3f
        ImGui.tableSetColumnIndex(0)
        imgui.ImGui.pushItemWidth(fieldWidth)
        ImGui.text("Position")
        ImGui.tableSetColumnIndex(1)
        imgui.ImGui.pushItemWidth(fieldWidth)
        if (ImGui.dragFloat("##position##X", positionArrayX)) {
            position.x = positionArrayX[0]
        }
        ImGui.tableSetColumnIndex(2)
        imgui.ImGui.pushItemWidth(fieldWidth)
        if (ImGui.dragFloat("##position##Y", positionArrayY)) {
            position.y = positionArrayY[0]
        }
        imgui.ImGui.popItemWidth()
        ImGui.tableNextRow()

        ImGui.tableSetColumnIndex(0)
        imgui.ImGui.pushItemWidth(fieldWidth)
        ImGui.text("Scale")
        ImGui.tableSetColumnIndex(1)
        imgui.ImGui.pushItemWidth(fieldWidth)
        if (ImGui.dragFloat("##scale##X", scaleArrayX)) {
            scale.x = scaleArrayX[0]
        }
        ImGui.tableSetColumnIndex(2)
        imgui.ImGui.pushItemWidth(fieldWidth)
        if (ImGui.dragFloat("##scale##Y", scaleArrayY)) {
            scale.y = scaleArrayY[0]
        }
        imgui.ImGui.popItemWidth()
        ImGui.tableNextRow()

        ImGui.tableSetColumnIndex(0)
        imgui.ImGui.pushItemWidth(fieldWidth)
        ImGui.text("Rotation / Z")
        ImGui.tableSetColumnIndex(1)
        imgui.ImGui.pushItemWidth(fieldWidth)
        if (ImGui.dragFloat("##rotation", rotationArray)) {
            rotation = rotationArray[0]
        }
        ImGui.tableSetColumnIndex(2)
        imgui.ImGui.pushItemWidth(fieldWidth)
        if (ImGui.dragFloat("##z", zArray)) {
            z = zArray[0]
        }
        imgui.ImGui.popItemWidth()
        ImGui.endTable()
    }
}