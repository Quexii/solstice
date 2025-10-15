package cc.lapiz.solstice.core.serialization

import cc.lapiz.solstice.data.Color
import cc.lapiz.solstice.ui.imgui.ImGui
import cc.lapiz.solstice.utils.argbToAbgr
import imgui.ImVec4
import imgui.flag.ImGuiCol
import kotlinx.serialization.Serializable

@Serializable
data class ImGuiThemeJSON(
    val Text: String,
    val TextDisabled: String,
    val WindowBg: String,
    val ChildBg: String,
    val PopupBg: String,
    val Border: String,
    val BorderShadow: String,
    val FrameBg: String,
    val FrameBgHovered: String,
    val FrameBgActive: String,
    val TitleBg: String,
    val TitleBgActive: String,
    val TitleBgCollapsed: String,
    val MenuBarBg: String,
    val ScrollbarBg: String,
    val ScrollbarGrab: String,
    val ScrollbarGrabHovered: String,
    val ScrollbarGrabActive: String,
    val CheckMark: String,
    val SliderGrab: String,
    val SliderGrabActive: String,
    val Button: String,
    val ButtonHovered: String,
    val ButtonActive: String,
    val Header: String,
    val HeaderHovered: String,
    val HeaderActive: String,
    val Separator: String,
    val SeparatorHovered: String,
    val SeparatorActive: String,
    val ResizeGrip: String,
    val ResizeGripHovered: String,
    val ResizeGripActive: String,
    val Tab: String,
    val TabHovered: String,
    val TabActive: String,
    val TabUnfocused: String,
    val TabUnfocusedActive: String,
    val DockingPreview: String,
    val DockingEmptyBg: String,
    val PlotLines: String,
    val PlotLinesHovered: String,
    val PlotHistogram: String,
    val PlotHistogramHovered: String,
    val TableHeaderBg: String,
    val TableBorderStrong: String,
    val TableBorderLight: String,
    val TableRowBg: String,
    val TableRowBgAlt: String,
    val TextSelectedBg: String,
    val DragDropTarget: String,
    val NavHighlight: String,
    val NavWindowingHighlight: String,
    val NavWindowingDimBg: String,
    val ModalWindowDimBg: String
)

fun ImGuiThemeJSON.applyToImGui() {
    val style = ImGui.style()

    fun String.toColorInt(): Int {
        val clean = this.removePrefix("#")
        val long = clean.toLong(16)
        return Color.HEX(long).toInt().argbToAbgr()
    }

    style.setColor(ImGuiCol.Text,Text.toColorInt())
    style.setColor(ImGuiCol.TextDisabled,TextDisabled.toColorInt())
    style.setColor(ImGuiCol.WindowBg,WindowBg.toColorInt())
    style.setColor(ImGuiCol.ChildBg,ChildBg.toColorInt())
    style.setColor(ImGuiCol.PopupBg,PopupBg.toColorInt())
    style.setColor(ImGuiCol.Border,Border.toColorInt())
    style.setColor(ImGuiCol.BorderShadow,BorderShadow.toColorInt())
    style.setColor(ImGuiCol.FrameBg,FrameBg.toColorInt())
    style.setColor(ImGuiCol.FrameBgHovered,FrameBgHovered.toColorInt())
    style.setColor(ImGuiCol.FrameBgActive,FrameBgActive.toColorInt())
    style.setColor(ImGuiCol.TitleBg,TitleBg.toColorInt())
    style.setColor(ImGuiCol.TitleBgActive,TitleBgActive.toColorInt())
    style.setColor(ImGuiCol.TitleBgCollapsed,TitleBgCollapsed.toColorInt())
    style.setColor(ImGuiCol.MenuBarBg,MenuBarBg.toColorInt())
    style.setColor(ImGuiCol.ScrollbarBg,ScrollbarBg.toColorInt())
    style.setColor(ImGuiCol.ScrollbarGrab,ScrollbarGrab.toColorInt())
    style.setColor(ImGuiCol.ScrollbarGrabHovered,ScrollbarGrabHovered.toColorInt())
    style.setColor(ImGuiCol.ScrollbarGrabActive,ScrollbarGrabActive.toColorInt())
    style.setColor(ImGuiCol.CheckMark,CheckMark.toColorInt())
    style.setColor(ImGuiCol.SliderGrab,SliderGrab.toColorInt())
    style.setColor(ImGuiCol.SliderGrabActive,SliderGrabActive.toColorInt())
    style.setColor(ImGuiCol.Button,Button.toColorInt())
    style.setColor(ImGuiCol.ButtonHovered,ButtonHovered.toColorInt())
    style.setColor(ImGuiCol.ButtonActive,ButtonActive.toColorInt())
    style.setColor(ImGuiCol.Header,Header.toColorInt())
    style.setColor(ImGuiCol.HeaderHovered,HeaderHovered.toColorInt())
    style.setColor(ImGuiCol.HeaderActive,HeaderActive.toColorInt())
    style.setColor(ImGuiCol.Separator,Separator.toColorInt())
    style.setColor(ImGuiCol.SeparatorHovered,SeparatorHovered.toColorInt())
    style.setColor(ImGuiCol.SeparatorActive,SeparatorActive.toColorInt())
    style.setColor(ImGuiCol.ResizeGrip,ResizeGrip.toColorInt())
    style.setColor(ImGuiCol.ResizeGripHovered,ResizeGripHovered.toColorInt())
    style.setColor(ImGuiCol.ResizeGripActive,ResizeGripActive.toColorInt())
    style.setColor(ImGuiCol.Tab,Tab.toColorInt())
    style.setColor(ImGuiCol.TabHovered,TabHovered.toColorInt())
    style.setColor(ImGuiCol.TabActive,TabActive.toColorInt())
    style.setColor(ImGuiCol.TabUnfocused,TabUnfocused.toColorInt())
    style.setColor(ImGuiCol.TabUnfocusedActive,TabUnfocusedActive.toColorInt())
    style.setColor(ImGuiCol.DockingPreview,DockingPreview.toColorInt())
    style.setColor(ImGuiCol.DockingEmptyBg,DockingEmptyBg.toColorInt())
    style.setColor(ImGuiCol.PlotLines,PlotLines.toColorInt())
    style.setColor(ImGuiCol.PlotLinesHovered,PlotLinesHovered.toColorInt())
    style.setColor(ImGuiCol.PlotHistogram,PlotHistogram.toColorInt())
    style.setColor(ImGuiCol.PlotHistogramHovered,PlotHistogramHovered.toColorInt())
    style.setColor(ImGuiCol.TableHeaderBg,TableHeaderBg.toColorInt())
    style.setColor(ImGuiCol.TableBorderStrong,TableBorderStrong.toColorInt())
    style.setColor(ImGuiCol.TableBorderLight,TableBorderLight.toColorInt())
    style.setColor(ImGuiCol.TableRowBg,TableRowBg.toColorInt())
    style.setColor(ImGuiCol.TableRowBgAlt,TableRowBgAlt.toColorInt())
    style.setColor(ImGuiCol.TextSelectedBg,TextSelectedBg.toColorInt())
    style.setColor(ImGuiCol.DragDropTarget,DragDropTarget.toColorInt())
    style.setColor(ImGuiCol.NavHighlight,NavHighlight.toColorInt())
    style.setColor(ImGuiCol.NavWindowingHighlight,NavWindowingHighlight.toColorInt())
    style.setColor(ImGuiCol.NavWindowingDimBg,NavWindowingDimBg.toColorInt())
    style.setColor(ImGuiCol.ModalWindowDimBg,ModalWindowDimBg.toColorInt())
}