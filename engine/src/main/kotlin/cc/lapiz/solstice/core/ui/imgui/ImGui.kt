package cc.lapiz.solstice.ui.imgui

import cc.lapiz.solstice.core.serialization.ImGuiThemeJSON
import cc.lapiz.solstice.core.serialization.applyToImGui
import cc.lapiz.solstice.core.ui.Colors
import cc.lapiz.solstice.data.Vector2
import cc.lapiz.solstice.resource.IO
import cc.lapiz.solstice.window.*
import imgui.ImGui
import imgui.ImGuiIO
import imgui.ImVec2
import imgui.flag.ImGuiCol
import imgui.flag.ImGuiConfigFlags
import imgui.gl3.*
import imgui.glfw.*
import imgui.internal.*
import imgui.type.*
import kotlinx.serialization.json.Json
import org.lwjgl.glfw.GLFW

private typealias NImGui = ImGui

object ImGui {
	private val imGuiGlfw = ImGuiImplGlfw()
	private val imGuiGl3 = ImGuiImplGl3()

	fun begin(name: String, vararg flags: Int) = NImGui.begin(name, flags.fold(0) { acc, flag -> acc or flag })
	fun end() = NImGui.end()
	fun text(text: String) = NImGui.text(text)
	fun button(label: String) = NImGui.button(label)
	fun button(label: String, size: ImVec2) = NImGui.button(label, size)
	fun invisibleButton(label: String, size: Vector2) = NImGui.invisibleButton(label, ImVec2(size.x, size.y))
	fun sameLine() = NImGui.sameLine()
	fun inputText(label: String, buffer: ImString) = NImGui.inputText(label, buffer)
	fun inputText(label: String, buffer: ImString, flags: Int) = NImGui.inputText(label, buffer, flags)
	fun checkbox(label: String, v: ImBoolean) = NImGui.checkbox(label, v)
	fun sliderFloat(label: String, v: FloatArray, vMin: Float, vMax: Float) = NImGui.sliderFloat(label, v, vMin, vMax)
	fun colorEdit3(label: String, color: FloatArray) = NImGui.colorEdit3(label, color)
	fun colorEdit4(label: String, color: FloatArray) = NImGui.colorEdit4(label, color)
	fun beginChild(id: String, width: Float, height: Float, border: Boolean) =
		NImGui.beginChild(id, width, height, border)

	fun endChild() = NImGui.endChild()

	fun beginTable(id: String, columns: Int, flags: Int) = NImGui.beginTable(id, columns, flags)
	fun tableNextRow() = NImGui.tableNextRow()
	fun tableSetColumnIndex(index: Int) = NImGui.tableSetColumnIndex(index)
	fun endTable() = NImGui.endTable()
	fun dragFloat(label: String, value: FloatArray) = NImGui.dragFloat(label, value)
	fun collapsingHeader(label: String) = NImGui.collapsingHeader(label)
	fun setNextWindowSize(width: Float, height: Float) = NImGui.setNextWindowSize(width, height)
	fun setNextWindowPos(x: Float, y: Float) = NImGui.setNextWindowPos(x, y)
	fun beginPopupContextItem(strId: String?) = NImGui.beginPopupContextItem(strId)
	fun openPopup(strId: String) = NImGui.openPopup(strId)
	fun closeCurrentPopup() = NImGui.closeCurrentPopup()
	fun selectable(label: String) = NImGui.selectable(label)
	fun image(userTextureId: Int, width: Float, height: Float) = NImGui.image(userTextureId.toLong(), width, height)
	fun image(userTextureId: Int, width: Float, height: Float, uv0x: Float, uv0y: Float, uv1x: Float, uv1y: Float) =
		NImGui.image(userTextureId.toLong(), width, height, uv0x, uv0y, uv1x, uv1y)

	fun pushID(id: String) = NImGui.pushID(id)
	fun popID() = NImGui.popID()
	fun getColorU32(idx: Int) = NImGui.getColorU32(idx)
	fun getWindowDrawList() = NImGui.getWindowDrawList()
	fun getItemRectMin() = NImGui.getItemRectMin()
	fun getItemRectMax() = NImGui.getItemRectMax()
	fun getItemRectSize() = NImGui.getItemRectSize()
	fun calcTextSize(text: String) = NImGui.calcTextSize(text)
	fun isItemHovered() = NImGui.isItemHovered()
	fun isItemClicked() = NImGui.isItemClicked()
	fun isItemActive() = NImGui.isItemActive()

	fun init(config: (ImGuiIO) -> Unit): ImGuiContext {
		val ctx = NImGui.createContext()
		config(io())
		imGuiGlfw.init(Display.handle, true)
		imGuiGl3.init("#version 330 core")
		applyStyle()
		return ctx
	}

	private fun applyStyle() = style().apply {
		this.windowRounding = 6f;
		this.childRounding = 4f;
		this.frameRounding = 4f;
		this.popupRounding = 6f;
		this.scrollbarRounding = 2f;
		this.grabRounding = 2f;
		this.tabRounding = 4f;

		this.windowPadding.set(10f, 10f);
		this.framePadding.set(6f, 5f);
		this.cellPadding.set(4f, 2f);
		this.itemSpacing.set(8f, 6f);
		this.itemInnerSpacing.set(4f, 3f);

		this.windowBorderSize = 1f;
		this.childBorderSize = 0f;
		this.popupBorderSize = 1f;
		this.frameBorderSize = 0f;
		this.tabBorderSize = 0f;


		val string = IO.getText("one_dark_imgui.json")
		Json.decodeFromString<ImGuiThemeJSON>(string).applyToImGui()
	}

	fun newFrame() {
		imGuiGl3.newFrame()
		imGuiGlfw.newFrame()
		NImGui.newFrame()
		ImGui.dockSpaceOverViewport()
	}

	fun render() {
		NImGui.render()
		imGuiGl3.renderDrawData(NImGui.getDrawData())

		if (io().configFlags and ImGuiConfigFlags.ViewportsEnable != 0) {
			val backupCurrentContext = GLFW.glfwGetCurrentContext()
			NImGui.updatePlatformWindows()
			NImGui.renderPlatformWindowsDefault()
			GLFW.glfwMakeContextCurrent(backupCurrentContext)
		}
	}

	fun cleanup() {
		imGuiGl3.shutdown()
		NImGui.destroyContext()
	}

	fun io() = NImGui.getIO()
	fun style() = NImGui.getStyle()

	fun styleColorsDark() = NImGui.styleColorsDark()
	fun styleColorsClassic() = NImGui.styleColorsClassic()
	fun styleColorsLight() = NImGui.styleColorsLight()

	fun isKeyPressed(key: Int) = NImGui.isKeyPressed(key)
	fun isMouseClicked(button: Int) = NImGui.isMouseClicked(button)
	fun isMouseDown(button: Int) = NImGui.isMouseDown(button)
	fun isMouseReleased(button: Int) = NImGui.isMouseReleased(button)
	fun getMousePosX() = NImGui.getMousePosX()
	fun getMousePosY() = NImGui.getMousePosY()
}