package cc.lapiz.solstice.core.ui

import cc.lapiz.solstice.utils.argbToAbgr
import imgui.ImGui
import imgui.ImVec2
import imgui.flag.ImGuiCol
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean

object EditorUI {
    object Window {
        fun begin(
            title: String,
            open: ImBoolean? = null,
            flags: Int = ImGuiWindowFlags.None
        ): Boolean {
            return ImGui.begin(title, open, flags)
        }

        fun end() {
            ImGui.end()
        }

        inline fun use(
            title: String,
            open: ImBoolean? = null,
            flags: Int = ImGuiWindowFlags.None,
            block: () -> Unit
        ) {
            if (begin(title, open, flags)) {
                try {
                    block()
                } finally {
                    end()
                }
            }
        }
    }

    object Child {
        fun begin(
            id: String,
            size: ImVec2 = ImVec2(0f, 0f),
            border: Boolean = false,
            flags: Int = ImGuiWindowFlags.None
        ): Boolean {
            return ImGui.beginChild(id, size, border, flags)
        }

        fun end() {
            ImGui.endChild()
        }

        inline fun use(
            id: String,
            size: ImVec2 = ImVec2(0f, 0f),
            border: Boolean = false,
            flags: Int = ImGuiWindowFlags.None,
            block: () -> Unit
        ) {
            if (begin(id, size, border, flags)) {
                try {
                    block()
                } finally {
                    end()
                }
            }
        }
    }

    // Button components
    object Button {
        fun primary(label: String, size: ImVec2 = ImVec2(0f, 0f)): Boolean {
            return ImGui.button(label, size)
        }

        fun secondary(label: String, size: ImVec2 = ImVec2(0f, 0f)): Boolean {
            return ImGui.button(label, size)
        }

        fun success(label: String, size: ImVec2 = ImVec2(0f, 0f)): Boolean {
            return ImGui.button(label, size)
        }

        fun warning(label: String, size: ImVec2 = ImVec2(0f, 0f)): Boolean {
            return ImGui.button(label, size)
        }

        fun error(label: String, size: ImVec2 = ImVec2(0f, 0f)): Boolean {
            return ImGui.button(label, size)
        }
    }

    // Text components
    object Text {
        fun primary(text: String) {
            ImGui.pushStyleColor(ImGuiCol.Text, Colors.TextPrimary.toInt().argbToAbgr())
            ImGui.text(text)
            ImGui.popStyleColor()
        }

        fun secondary(text: String) {
            ImGui.pushStyleColor(ImGuiCol.Text, Colors.TextSecondary.toInt().argbToAbgr())
            ImGui.text(text)
            ImGui.popStyleColor()
        }

        fun disabled(text: String) {
            ImGui.pushStyleColor(ImGuiCol.Text, Colors.TextDisabled.toInt().argbToAbgr())
            ImGui.text(text)
            ImGui.popStyleColor()
        }

        fun success(text: String) {
            ImGui.pushStyleColor(ImGuiCol.Text, Colors.Success.toInt().argbToAbgr())
            ImGui.text(text)
            ImGui.popStyleColor()
        }

        fun warning(text: String) {
            ImGui.pushStyleColor(ImGuiCol.Text, Colors.Warning.toInt().argbToAbgr())
            ImGui.text(text)
            ImGui.popStyleColor()
        }

        fun error(text: String) {
            ImGui.pushStyleColor(ImGuiCol.Text, Colors.Error.toInt().argbToAbgr())
            ImGui.text(text)
            ImGui.popStyleColor()
        }

        fun info(text: String) {
            ImGui.pushStyleColor(ImGuiCol.Text, Colors.Info.toInt().argbToAbgr())
            ImGui.text(text)
            ImGui.popStyleColor()
        }

        fun accent(text: String) {
            ImGui.pushStyleColor(ImGuiCol.Text, Colors.Accent.toInt().argbToAbgr())
            ImGui.text(text)
            ImGui.popStyleColor()
        }
    }

    // Frame components (grouping)
    object Frame {
        fun begin(label: String = "") {
            ImGui.beginGroup()
            if (label.isNotEmpty()) {
                Text.secondary(label)
            }
        }

        fun end() {
            ImGui.endGroup()
        }

        inline fun use(label: String = "", block: () -> Unit) {
            begin(label)
            try {
                block()
            } finally {
                end()
            }
        }
    }

    // Separator with custom styling
    object Separator {
        fun draw() {
            ImGui.pushStyleColor(ImGuiCol.Separator, Colors.Border.toInt().argbToAbgr())
            ImGui.separator()
            ImGui.popStyleColor()
        }

        fun text(label: String) {
            ImGui.pushStyleColor(ImGuiCol.Text, Colors.TextSecondary.toInt().argbToAbgr())
            ImGui.pushStyleColor(ImGuiCol.Separator, Colors.Border.toInt().argbToAbgr())
            ImGui.separatorText(label)
            ImGui.popStyleColor(2)
        }
    }
}