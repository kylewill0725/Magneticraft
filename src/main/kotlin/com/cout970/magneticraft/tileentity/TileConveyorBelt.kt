package com.cout970.magneticraft.tileentity

import com.cout970.magneticraft.api.conveyor.IConveyorBelt
import com.cout970.magneticraft.api.conveyor.IItemBox
import com.cout970.magneticraft.registry.ITEM_HANDLER
import com.cout970.magneticraft.tileentity.TileBase
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.ItemStackHandler

/**
 * Created by Qwyll on 10/2/2016.
 */
class TileConveyorBelt : TileBase(), ITickable, IConveyorBelt {
    val inventory = Inventory()

    override fun load(nbt: NBTTagCompound) {
//        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update() {
//        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save(): NBTTagCompound {
//        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
        return NBTTagCompound()
    }

    override fun hasCapability(capability: Capability<*>?, facing: EnumFacing?): Boolean {
        if (capability == ITEM_HANDLER) return true
        return super.hasCapability(capability, facing)
    }

    override fun <T : Any?> getCapability(capability: Capability<T>?, facing: EnumFacing?): T {
        if (capability == ITEM_HANDLER) return inventory as T
        return super.getCapability(capability, facing)
    }

    override fun addItem(direction: EnumFacing, pos: BlockPos, itemBox: IItemBox) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeItem(itemBox: IItemBox, laneNum: Int, simulated: Boolean) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLaneCount(): Int {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLane(laneNum: Int) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDir(): EnumFacing {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun extract(itemBox: IItemBox, laneNum: Int, simulated: Boolean): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun inject(pos: BlockPos, itemBox: IItemBox, laneNum: Int, simulated: Boolean): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getParent(): TileEntity {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLevel(): Int {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChange() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class Inventory : ItemStackHandler(4) {
        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack? {
            if (slot >= 0 && slot < 4)
                return super.insertItem(slot, stack, simulate)
            return stack
        }

        override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack? {
            if (slot >= 0 && slot < 4)
                return super.extractItem(slot, amount, simulate)
            return null
        }
    }
}