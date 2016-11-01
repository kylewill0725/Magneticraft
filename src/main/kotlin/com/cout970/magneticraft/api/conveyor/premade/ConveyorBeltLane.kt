package com.cout970.magneticraft.api.conveyor.premade

import coffee.cypher.mcextlib.extensions.vectors.toBlockPos
import coffee.cypher.mcextlib.extensions.vectors.toDoubleVec
import com.cout970.magneticraft.api.conveyor.IConveyorBelt
import com.cout970.magneticraft.api.conveyor.IConveyorBeltLane
import com.cout970.magneticraft.api.conveyor.IItemBox
import com.cout970.magneticraft.util.plus
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.tileentity.TileEntity
import java.util.*

/**
 * Created by Qwyll on 10/29/2016.
 */
class ConveyorBeltLane : IConveyorBeltLane {
    private var content: MutableList<IItemBox> = ArrayList()
    private var hitBox = HitBoxArray()
    private var parent: IConveyorBelt
    private var lane: Int

    constructor(parent: IConveyorBelt, lane: Int) {
        this.parent = parent
        this.lane = lane
    }

    fun advance(b: IItemBox) {
        setHitBoxSpace(b.getPosition(), false)
        if (hasHitBoxSpace(b.getPosition() + 1)) {
            b.setPosition(b.getPosition() + 1)
        }
        setHitBoxSpace(b.getPosition(), true)
    }

    override fun getLane() = this.lane

    override fun getConveyorBelt() = parent

    override fun getItemBoxes() = content

    override fun getHitBoxes() = hitBox

    override fun setHitBoxSpace(pos: Int, value: Boolean) {
        for (i in 0..3) {
            if (i + pos >= hitBox.size()) {
                var tile: TileEntity = getFrontConveyor(parent)
                setHitBoxSpaceExtern(tile!!, i + pos - 16, value)
            } else {
                hitBox.setOccupied(pos + i, value)
            }
        }
    }

    override fun setHitBoxSpaceExtern(tile: TileEntity, pos: Int, value: Boolean) {
        if (tile is IConveyorBelt) {
            val iter = IConveyorBelt.BeltInteraction.InterBelt(parent.getDir(), tile.getDir())
            when(iter) {
                IConveyorBelt.BeltInteraction.DIRECT -> tile.getLane(lane).getHitBoxes().setOccupied(pos % 16, value)
                IConveyorBelt.BeltInteraction.LEFT_T -> for (i in (8*lane+2) until (8*lane+6)) tile.getLane(0).getHitBoxes().setOccupied(i % 16, value)
                IConveyorBelt.BeltInteraction.RIGHT_T -> for (i in (-8*lane+10) until (-8*lane+14)) tile.getLane(parent.getLaneCount() - 1).getHitBoxes().setOccupied(i % 16, value)
            }
        }
    }

    override fun hasHitBoxSpace(pos: Int): Boolean { //SPEEDTEST
        for (i in 0..3) {
            if (i + pos >= hitBox.size()) {
                val tile = getFrontConveyor(parent)
                return hasHitBoxSpaceExtern(tile, i + pos - 16)
            } else {
                return !hitBox.isOccupied(pos + i)
            }
        }
        return true
    }

    override fun hasHitBoxSpaceExtern(tile: TileEntity, pos: Int): Boolean {
        var hasSpace = false
        if (tile is IConveyorBelt) {
            val iter = IConveyorBelt.BeltInteraction.InterBelt(parent.getDir(), tile.getDir())
            when(iter) {
                IConveyorBelt.BeltInteraction.DIRECT -> hasSpace = !tile.getLane(lane).getHitBoxes().isOccupied(pos % 16)
                IConveyorBelt.BeltInteraction.LEFT_T -> for (i in (8*lane+2) until (8*lane+6)) hasSpace = hasSpace or !tile.getLane(parent.getLaneCount() - 1).getHitBoxes().isOccupied(i % 16)
                IConveyorBelt.BeltInteraction.RIGHT_T -> for (i in (-8*lane+10) until (-8*lane+14)) hasSpace = hasSpace or !tile.getLane(0).getHitBoxes().isOccupied(i % 16)
            }
        }
        return hasSpace
    }

    override fun save(nbt: NBTTagCompound) {
        for (i in 0 until hitBox.size()) {
            nbt.setBoolean("Lane_${lane}_${i}", hitBox.isOccupied(i))
        }
        val list = NBTTagList()
        nbt.setInteger("${lane}_Size", content.size)
        for (aContent: IItemBox in content) {
            val t = NBTTagCompound()
            aContent.save(t)
            list.appendTag(t)
        }
        nbt.setTag("${lane}_Boxes", list)
    }

    override fun load(nbt: NBTTagCompound) { //SPEEDTEST
        content.clear()
        for (i in 0 until hitBox.size()) {
            getHitBoxes().setOccupied(i, nbt.getBoolean("Lane_${lane}_${i}"))
        }
        val content_size = nbt.getInteger("${lane}_Size")
        val list = nbt.getTagList("${lane}_Boxes", 10) //10 is the array index of a NBTCompound
        for (i in 0 until content_size) {
            val t = list.getCompoundTagAt(i)
            if (t.hasKey("Lane")) {
                content.add(ItemBox(t))
            }
        }
    }

    companion object {
        fun getFrontConveyor(c: IConveyorBelt): TileEntity {
            val t = c.getParent()
            var retVal: TileEntity
            when (c.getSlope()) {
                1 -> retVal = t.world.getTileEntity(t.pos.add(c.getDir().frontOffsetX, c.getDir().frontOffsetY + 1, c.getDir().frontOffsetZ)) ?: throw Exception("This up ramp is missing an exit.")
                -1 -> retVal = t.world.getTileEntity(t.pos.add(c.getDir().frontOffsetX, c.getDir().frontOffsetY - 1, c.getDir().frontOffsetZ)) ?: throw Exception("This down ramp is missing an exit.")
                0 -> retVal = t.world.getTileEntity(t.pos.add(c.getDir().frontOffsetX, c.getDir().frontOffsetY, c.getDir().frontOffsetZ)) ?: throw Exception("This conveyor is missing an exit.")
                else -> throw Exception("Invalid slope")
            }
            return retVal
        }
    }
}