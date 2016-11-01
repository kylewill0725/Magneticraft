package com.cout970.magneticraft.api.conveyor

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity

/**
 * Created by Qwyll on 10/29/2016.
 */
interface IConveyorBeltLane {

    fun getConveyorBelt(): IConveyorBelt

    fun getItemBoxes(): MutableList<IItemBox>

    fun getHitBoxes(): IHitBoxArray

    fun getLane(): Int

    fun setHitBoxSpace(pos: Int, value: Boolean)

    fun setHitBoxSpaceExtern(tile: TileEntity, pos: Int, value: Boolean)

    fun hasHitBoxSpace(pos: Int): Boolean

    fun hasHitBoxSpaceExtern(tile: TileEntity, pos: Int): Boolean

    fun save(nbt: NBTTagCompound)

    fun load(nbt: NBTTagCompound)
}