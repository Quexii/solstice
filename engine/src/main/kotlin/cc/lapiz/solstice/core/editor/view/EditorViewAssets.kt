package cc.lapiz.solstice.core.editor.view

import cc.lapiz.solstice.core.assets.Assets
import cc.lapiz.solstice.core.assets.meta.MetaTexture
import cc.lapiz.solstice.core.assets.meta.MetaTextureFilter
import cc.lapiz.solstice.core.assets.meta.MetaTextureType
import cc.lapiz.solstice.core.assets.meta.MetaTextureWrap
import cc.lapiz.solstice.core.assets.types.Sprite
import cc.lapiz.solstice.core.editor.Editor
import cc.lapiz.solstice.core.editor.InspectorItem
import cc.lapiz.solstice.core.ui.imgui.GridItem
import cc.lapiz.solstice.core.ui.imgui.ImGuiCtx
import cc.lapiz.solstice.core.ui.imgui.dynamicItemGrid
import imgui.type.ImInt

class EditorViewAssets: EditorView("Assets") {
    override fun draw() {
        ImGuiCtx.window("Assets") {
            val list = Assets.getAll()
            dynamicItemGrid(
                items = list.map {
                    GridItem(it::class.java.simpleName, icon = { min, max ->
                        when (it) {
                            is Sprite -> addImage(it.textureId().toLong(), min.plus(8f, 8f), max.minus(8f, 8f))
                        }
                    })
                },
                maxItemSize = 100f,
                onItemDoubleClick = { index, item ->
                    Editor.selectInspectorItem(
                        InspectorItem(list[index], when (list[index]) {
                        is Sprite -> arrayOf(
                            ImInt(MetaTextureType.entries.indexOf(((Editor.inspectorItem!!.data!! as Sprite).meta as MetaTexture.Serialized).type)),
                            ImInt(MetaTextureFilter.entries.indexOf(((Editor.inspectorItem!!.data!! as Sprite).meta as MetaTexture.Serialized).filter)),
                            ImInt(MetaTextureWrap.entries.indexOf(((Editor.inspectorItem!!.data!! as Sprite).meta as MetaTexture.Serialized).wrap))
                        )

                        else -> {}
                    })
                    )
                }
            )
        }
    }
}