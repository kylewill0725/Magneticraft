package com.cout970.magneticraft.item

import com.cout970.magneticraft.misc.CreativeTabMg
import com.cout970.magneticraft.util.MODID
import com.cout970.magneticraft.util.resource
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item

abstract class ItemBase(
        registryName: String,
        unlocalizedName: String = registryName
) : Item() {

    init {
        this.registryName = resource(registryName)
        this.unlocalizedName = "$MODID.$unlocalizedName"
        creativeTab = CreativeTabMg
    }

    open fun getMaxModels(): Int = 1

    open fun getModelLoc(i: Int): ModelResourceLocation = ModelResourceLocation(registryName, "inventory")
}