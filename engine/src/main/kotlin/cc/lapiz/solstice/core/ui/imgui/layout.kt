package cc.lapiz.solstice.core.ui.imgui

import cc.lapiz.solstice.core.data.Color
import imgui.*
import imgui.flag.*
import imgui.type.*

data class GridItem(
    val label: String,
    val icon: ImDrawList.(ImVec2, ImVec2) -> Unit = { _, _ -> },
    val color: Int = ImGui.getColorU32(ImGuiCol.MenuBarBg),
    val data: Any? = null
)

fun dynamicItemGrid(
    items: List<GridItem>,
    maxItemSize: Float = 120f,
    minItemSize: Float = 60f,
    onItemClick: (index: Int, item: GridItem) -> Unit = { _, _ -> },
    onItemDoubleClick: (index: Int, item: GridItem) -> Unit = { _, _ -> }
) {
    val availableWidth = ImGui.getContentRegionAvail().x
    val style = ImGui.getStyle()

    val itemsPerRow =
        maxOf(1, ((availableWidth + style.itemSpacing.x) / (maxItemSize + style.itemSpacing.x)).toInt())
    val actualItemSize = ((availableWidth - (itemsPerRow - 1) * style.itemSpacing.x) / itemsPerRow)
        .coerceIn(minItemSize, maxItemSize)

    for ((index, item) in items.withIndex()) {
        if (index > 0 && index % itemsPerRow != 0) {
            ImGui.sameLine()
        }

        drawGridItem(item, index, actualItemSize, onItemClick, onItemDoubleClick)
    }
}

private fun drawGridItem(
    item: GridItem,
    index: Int,
    size: Float,
    onItemClick: (Int, GridItem) -> Unit,
    onItemDoubleClick: (Int, GridItem) -> Unit
) {
    val id = "##grid_item_$index"

    ImGui.invisibleButton(id, ImVec2(size, size))

    val isHovered = ImGui.isItemHovered()
    val isClicked = ImGui.isItemClicked()
    val isDoubleClicked = ImGui.isMouseDoubleClicked(ImGuiMouseButton.Left) && isHovered

    val min = ImGui.getItemRectMin()
    val max = ImGui.getItemRectMax()
    val drawList = ImGui.getWindowDrawList()

    val bgColor = when {
        isHovered -> ImGui.getColorU32(ImGuiCol.FrameBgHovered)
        else -> ImGui.getColorU32(ImGuiCol.FrameBg)
    }
    drawList.addRectFilled(min, max, bgColor, 6f)

    if (isHovered) {
        drawList.addRect(min, max, ImGui.getColorU32(ImGuiCol.TabActive), 6f, 0, 2f)
    }

    item.icon.invoke(drawList, min, max)

    val labelSize = ImGui.calcTextSize(item.label)
    val availableLabelWidth = size - 16f // Padding
    var displayLabel = item.label

    if (labelSize.x > availableLabelWidth) {
        displayLabel = "${item.label.take(10)}..."
    }

    val labelPos = ImVec2(
        min.x + (size - ImGui.calcTextSize(displayLabel).x) * 0.5f,
        min.y + size * 0.7f
    )
    drawList.addText(labelPos, ImGui.getColorU32(ImGuiCol.Text), displayLabel)

    if (isClicked) {
        onItemClick(index, item)
    }
    if (isDoubleClicked) {
        onItemDoubleClick(index, item)
    }
}

fun table2Column(title: String, block: () -> Unit) {
    if (ImGui.beginTable(title, 2, ImGuiTableFlags.BordersInnerV or ImGuiTableFlags.Resizable or ImGuiTableFlags.NoBordersInBody)) {
        ImGui.tableSetupColumn("Label", ImGuiTableColumnFlags.WidthFixed, 120f)
        ImGui.tableSetupColumn("Value", ImGuiTableColumnFlags.WidthStretch)

        block()

        ImGui.endTable()
    }
}

fun tableRow(label: String, value: Any?, draw: () -> Unit = { ImGui.textDisabled("<unsupported>") }) {
    ImGui.tableNextRow()
    ImGui.tableNextColumn()
    ImGui.text(label)
    ImGui.tableNextColumn()
    ImGui.setNextItemWidth(-1f)

    when (value) {
        is ImFloat -> ImGui.inputFloat("##$label", value)
        is ImInt -> ImGui.inputInt("##$label", value)
        is ImBoolean -> ImGui.checkbox("##$label", value)
        is ImString -> ImGui.inputText("##$label", value)
        is FloatArray -> when (value.size) {
            2 -> ImGui.inputFloat2(label, value)
            3 -> ImGui.inputFloat3(label, value)
            4 -> ImGui.inputFloat4(label, value)
        }
        is Color -> ImGui.colorEdit4(label, value.backing)

        else -> draw()
    }
}