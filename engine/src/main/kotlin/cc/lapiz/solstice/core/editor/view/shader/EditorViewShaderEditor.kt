package cc.lapiz.solstice.core.editor.view.shader

import cc.lapiz.solstice.core.data.Vector2
import cc.lapiz.solstice.core.editor.view.EditorView
import cc.lapiz.solstice.core.ui.imgui.ImGuiCtx
import imgui.ImGui
import imgui.ImVec2
import imgui.extension.imnodes.ImNodes
import imgui.flag.ImGuiColorEditFlags
import imgui.flag.ImGuiKey
import imgui.flag.ImGuiMouseButton
import imgui.type.ImBoolean
import imgui.type.ImInt
import imgui.type.ImString

class EditorViewShaderEditor : EditorView("Shader Editor") {
    private val graph: ShaderGraph? = ShaderGraph()
    private var clickPos = Vector2()

    override fun draw() {
        ImGuiCtx.window("Shader Editor") {
            graph?.let { dragGraph(it) }
        }
    }

    private fun dragGraph(graph: ShaderGraph) {
        ImNodes.beginNodeEditor()

        if (ImGui.isWindowHovered() &&
            ImGui.isMouseClicked(ImGuiMouseButton.Right)
        ) {
            clickPos = Vector2(ImGui.getMousePosX(), ImGui.getMousePosY())
            ImGui.openPopup("shader_editor_create_node")
        }
        if (ImGui.beginPopup("shader_editor_create_node")) {
            val size = ImGui.getContentRegionAvail()

            ImGui.text("Create New Node")
            NodeType.entries.forEach {
                if (ImGui.button(it.name, size.x, 24f)) {
                    ImGui.closeCurrentPopup()
                    graph.nodes.add(makeNode(it, graph, clickPos))
                }
            }
            ImGui.endPopup()
        }

        // Nodes
        for (n in graph.nodes) drawNode(graph, n)

        // Links
        for (l in graph.links) ImNodes.link(l.id, l.fromPin, l.toPin)

        // Create links (user drag from pin → pin)
        ImNodes.endNodeEditor()

        if (graph.nodes.isNotEmpty()) {
            val from = ImInt()
            val to = ImInt()
            if (ImNodes.isLinkCreated(from, to)) {
                if (canConnect(graph, from.get(), to.get())) {
                    graph.links += Link(graph.nextId++, from.get(), to.get())
                }
            }

            // Delete selected links/nodes via key or context menu
            val numSelected = ImNodes.numSelectedLinks()
            val linksBuf = IntArray(numSelected)
            if (numSelected > 0) {
                ImNodes.getSelectedLinks(linksBuf)
                if (ImGui.isKeyPressed(ImGuiKey.Delete)) {
                    graph.links.removeAll { it.id in linksBuf.toSet() }
                }
            }
        }
    }

    fun drawNode(graph: ShaderGraph, node: Node) {
        ImNodes.beginNode(node.id)
        ImNodes.beginNodeTitleBar()
        ImGui.text(node.type.name)
        ImNodes.endNodeTitleBar()

        // Inputs
        for (pin in node.inputs) {
            ImNodes.beginInputAttribute(pin.id)
            ImGui.text(pin.name)
            // optional inline param UI when not connected:
            if (!isPinConnected(graph, pin.id)) drawInlineParam(node, pin)
            ImNodes.endInputAttribute()
        }

        // Outputs
        for (pin in node.outputs) {
            ImNodes.beginOutputAttribute(pin.id)
            ImGui.text(pin.name)
            ImNodes.endOutputAttribute()
        }

        // Node params section
        drawNodeParamsPanel(node)

        ImNodes.endNode()

        // Position
        val p = ImVec2()
        ImNodes.getNodeEditorSpacePos(p, node.id)
        node.pos.x = p.x; node.pos.y = p.y
    }

    fun canConnect(g: ShaderGraph, a: Int, b: Int): Boolean {
        val (pa, pb) = findPin(g, a) to findPin(g, b)
        // must be input ↔ output
        if (pa.kind == pb.kind) return false
        val inPin = if (pa.kind == PinKind.INPUT) pa else pb
        val outPin = if (pa.kind == PinKind.OUTPUT) pa else pb

        // value type compatibility (allow vec->vec, float->float, implicit widen e.g., float to vecN?)
        return compatible(outPin.valueType, inPin.valueType) &&
                // prevent multiple links into the same input
                g.links.none { it.toPin == inPin.id }
    }

    fun compatible(a: ValueType, b: ValueType): Boolean {
        if (a == b) return true
        // implicit promotes
        if (a == ValueType.Float && b in setOf(ValueType.Vec2, ValueType.Vec3, ValueType.Vec4)) return true
        return false
    }

    fun findPin(g: ShaderGraph, pinId: Int): Pin =
        g.nodes.asSequence().flatMap { it.inputs.asSequence() + it.outputs.asSequence() }
            .first { it.id == pinId }

    fun isPinConnected(g: ShaderGraph, pinId: Int) =
        g.links.any { it.fromPin == pinId || it.toPin == pinId }

    fun makeNode(type: NodeType, graph: ShaderGraph, pos: Vector2): Node {
        val id = graph.nextId++
        val node = Node(id, type, pos)

        fun pin(name: String, t: ValueType, k: PinKind) =
            Pin(graph.nextId++, name, t, k).also {
                if (k == PinKind.INPUT) node.inputs += it else node.outputs += it
            }

        when (type) {
            NodeType.Time -> {
                pin("t", ValueType.Float, PinKind.OUTPUT)
            }

            NodeType.UV -> {
                pin("uv", ValueType.Vec2, PinKind.OUTPUT)
            }

            NodeType.Constant -> {
                node.params["value"] = 1.0f
                pin("value", ValueType.Float, PinKind.OUTPUT)
            }

            NodeType.Color -> {
                node.params["color"] = floatArrayOf(1f, 1f, 1f, 1f)
                pin("color", ValueType.Vec4, PinKind.OUTPUT)
            }

            NodeType.Texture2D -> {
                node.params["texturePath"] = "textures/white.png"
                pin("uv", ValueType.Vec2, PinKind.INPUT)
                pin("tex", ValueType.Sampler2D, PinKind.OUTPUT)
                pin("color", ValueType.Vec4, PinKind.OUTPUT)
            }

            NodeType.Mul -> {
                pin("A", ValueType.Vec4, PinKind.INPUT)
                pin("B", ValueType.Vec4, PinKind.INPUT)
                pin("Out", ValueType.Vec4, PinKind.OUTPUT)
            }

            NodeType.Lerp -> {
                pin("A", ValueType.Vec4, PinKind.INPUT)
                pin("B", ValueType.Vec4, PinKind.INPUT)
                pin("t", ValueType.Float, PinKind.INPUT)
                pin("Out", ValueType.Vec4, PinKind.OUTPUT)
            }

            NodeType.FragmentOutput -> {
                pin("Color", ValueType.Vec4, PinKind.INPUT)
            }

            else -> { /* add more math nodes similarly */
            }
        }
        return node
    }

    fun drawInlineParam(node: Node, pin: Pin) {
        when (node.type) {
            NodeType.Constant -> {
                val value = (node.params["value"] as? Float) ?: 0f
                val arr = floatArrayOf(value)
                ImGui.pushItemWidth(60f)
                if (ImGui.dragFloat("##${pin.id}", arr, 0.01f)) {
                    node.params["value"] = arr[0]
                }
                ImGui.popItemWidth()
            }

            NodeType.Color -> {
                val color = (node.params["color"] as? FloatArray) ?: floatArrayOf(1f, 1f, 1f, 1f)
                if (ImGui.colorEdit4(
                        "##${pin.id}", color,
                        ImGuiColorEditFlags.NoInputs or ImGuiColorEditFlags.NoLabel
                    )
                ) {
                    node.params["color"] = color.copyOf()
                }
            }

            NodeType.Texture2D -> {
                val texPath = node.params["texturePath"] as? String ?: ""
                ImGui.pushItemWidth(100f)
                if (ImGui.inputText("##${pin.id}", ImString(texPath))) {
                    node.params["texturePath"] = texPath
                }
                ImGui.popItemWidth()
            }

            else -> { /* no inline param */
            }
        }
    }

    fun drawNodeParamsPanel(node: Node) {
        // Add a visual separator only if there are params
        if (node.params.isEmpty()) return
        ImGui.separator()

        when (node.type) {
            NodeType.Texture2D -> {
                val path = node.params["texturePath"] as? String ?: ""
                val buf = ByteArray(256)
                val current = path.encodeToByteArray()
                System.arraycopy(current, 0, buf, 0, minOf(current.size, buf.size - 1))
                if (ImGui.inputText("Path##${node.id}", ImString(String(buf)))) {
                    node.params["texturePath"] = buf.decodeToString().trimEnd('\u0000')
                }

                var wrap = (node.params["wrap"] as? Boolean) ?: false
                if (ImGui.checkbox("Wrap##${node.id}", wrap)) {
                    node.params["wrap"] = !wrap
                }
            }

            NodeType.Lerp -> {
                var interp = (node.params["mode"] as? Int) ?: 0
                val modes = arrayOf("Linear", "Smoothstep")
                if (ImGui.beginCombo("Mode##${node.id}", modes[interp])) {
                    for (i in modes.indices) {
                        if (ImGui.selectable(modes[i], interp == i)) {
                            interp = i
                            node.params["mode"] = interp
                        }
                    }
                    ImGui.endCombo()
                }
            }

            // Color nodes may have extra toggles
            NodeType.Color -> {
                val hdr = (node.params["hdr"] as? Boolean) ?: false
                if (ImGui.checkbox("HDR##${node.id}", hdr)) node.params["hdr"] = !hdr
            }

            else -> { /* nothing for basic math nodes */
            }
        }
    }
}