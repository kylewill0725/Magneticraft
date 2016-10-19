package com.cout970.magneticraft.api.conveyor

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

/**
* Created by Qwyll on 10/2/2016.
*/
interface IItemBox {
    fun getContent(): ItemStack
    fun setContent(content: ItemStack)

    fun getPosition(): Int
    fun setPosition(positions: Int)

    fun getLane(): Int
    fun setLane(lane: Int)

    fun save(t: NBTTagCompound)
    fun load(t: NBTTagCompound)

    fun getLastUpdateTick(): Long
    fun setLastUpdateTick(tick: Long)
}