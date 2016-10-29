package com.cout970.magneticraft.api.conveyor.premade

import com.cout970.magneticraft.api.conveyor.IItemBox
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

/**
* Created by Qwyll on 10/29/2016.
*/
class ItemBox : IItemBox {

    private var itemStack: ItemStack
    private var pos: Int = 0
    private var lane: Int = 0
    private var lastTick: Long = 0

    constructor(it: ItemStack) {
        itemStack = it.copy()
    }

    constructor(t: NBTTagCompound) {
        itemStack = ItemStack.loadItemStackFromNBT(t)
        setPosition(t.getInteger("Pos"))
        setLane(t.getInteger("Lane"))
    }

    override fun getLane() = this.lane
    override fun setLane(lane: Int) {
        this.lane = lane
    }

    override fun getContent() = this.itemStack
    override fun setContent(content: ItemStack) {
        this.itemStack = content.copy()
    }

    override fun getPosition() = this.pos
    override fun setPosition(positions: Int) {
        this.pos = positions
    }

    override fun getLastUpdateTick() = this.lastTick
    override fun setLastUpdateTick(tick: Long) {
        lastTick = tick
    }

    override fun save(t: NBTTagCompound) {
        getContent().writeToNBT(t)
        t.setInteger("Pos", getPosition())
        t.setInteger("Lane", getLane())
    }

    override fun load(t: NBTTagCompound) {
        setContent(ItemStack.loadItemStackFromNBT(t))
        setPosition(t.getInteger("Pos"))
        setLane(t.getInteger("Lane"))
    }

}
