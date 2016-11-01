package com.cout970.magneticraft.tileentity

import coffee.cypher.mcextlib.extensions.vectors.isParallel
import com.cout970.magneticraft.api.conveyor.IConveyorBelt
import com.cout970.magneticraft.api.conveyor.IConveyorBeltLane
import com.cout970.magneticraft.api.conveyor.IItemBox
import com.cout970.magneticraft.api.conveyor.premade.ConveyorBeltLane
import com.cout970.magneticraft.block.PROPERTY_DIRECTION
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

    var lanes = Array(2, { i -> ConveyorBeltLane(this, i) })
    var time: Long = 0

    override fun update() {
        for (lane in lanes) advLane(lane)
        time = System.currentTimeMillis()
        if (worldObj.totalWorldTime % 200 == 0L)
            onChange()
    }

    private fun advLane(lane: ConveyorBeltLane) {
        if (worldObj.totalWorldTime % 10 == 0L) {
            lane.getHitBoxes().clear()
        }

        for (i in 0 until lane.getItemBoxes().size) {
            val itemBox = lane.getItemBoxes()[i]
            if (itemBox.getLastUpdateTick() != worldObj.totalWorldTime) {
                itemBox.setLastUpdateTick(worldObj.totalWorldTime)
                if (itemBox.getPosition() < 15) {
                    lane.advance(itemBox)
                    continue
                }
                val tile = ConveyorBeltLane.getFrontConveyor(this)
                if (tile is IConveyorBelt) {
                    val interaction = IConveyorBelt.BeltInteraction.InterBelt(getDir(), tile.getDir())
                    when (interaction) {
                        IConveyorBelt.BeltInteraction.DIRECT -> {
                            if (itemBox.getPosition() < 16) lane.advance(itemBox)
                            if (itemBox.getPosition() == 16) {
                                tile.getLane(lane.getLane()).setHitBoxSpace(0, false)
                                if (tile.inject(0, itemBox, lane.getLane(), false)) {
                                    extract(itemBox, lane.getLane(), false)
                                }
                            }
                        }
                        IConveyorBelt.BeltInteraction.LEFT_T -> {
                            if (tile.getSlope() == 0) {
                                if (itemBox.getPosition() < 18) lane.advance(itemBox)
                                if (itemBox.getPosition() >= 18) {
                                    val new_pos = if (lane.getLane() == 0) 2 else 10
                                    tile.getLane(getLaneCount() - 1).setHitBoxSpace(new_pos, false)
                                    if (tile.inject(new_pos, itemBox, getLaneCount() - 1, false)) {
                                        extract(itemBox, lane.getLane(), false)
                                    }
                                }
                            }
                        }
                        IConveyorBelt.BeltInteraction.RIGHT_T -> {
                            if (tile.getSlope() == 0) {
                                if (itemBox.getPosition() < 18) lane.advance(itemBox)
                                if (itemBox.getPosition() == 18) {
                                    val new_pos = if (lane.getLane() == 0) 10 else 2
                                    tile.getLane(0).setHitBoxSpace(new_pos, false)
                                    if (tile.inject(new_pos, itemBox, 0, false)) {
                                        extract(itemBox, lane.getLane(), false)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onChange() {
        sendUpdateToNearPlayers()
    }

    override fun save() = NBTTagCompound().apply {
        for (lane in lanes) lane.save(this)
    }

    override fun load(nbt: NBTTagCompound) {
        for (lane in lanes) lane.load(nbt)
    }

    override fun hasCapability(capability: Capability<*>?, facing: EnumFacing?): Boolean {
        if (capability == ITEM_HANDLER) return true
        return super.hasCapability(capability, facing)
    }

    override fun <T : Any?> getCapability(capability: Capability<T>?, facing: EnumFacing?): T {
        if (capability == ITEM_HANDLER) return this as T
        return super.getCapability(capability, facing)
    }

    override fun addItem(direction: EnumFacing, pos: Int, itemBox: IItemBox, sim: Boolean): Boolean {
//        if (itemBox == null) return false TODO delete if not needed
        val laneNum: Int
        if (!direction.isParallel(getDir())) {
            laneNum = if (direction == getDir().opposite.rotateAround(EnumFacing.Axis.Y)) 0 else (getLaneCount() - 1)
        } else {
            laneNum = itemBox.getLane()
        }
        return inject(pos, itemBox, laneNum, sim)
    }

    override fun inject(pos: Int, itemBox: IItemBox, laneNum: Int, simulated: Boolean): Boolean {
        val lane = getLane(laneNum)
        if (lane.hasHitBoxSpace(pos)) {
            if (!simulated) {
                itemBox.setPosition(pos)
                itemBox.setLane(laneNum)
                lane.setHitBoxSpace(pos, true)
                lane.getItemBoxes().add(itemBox)
            }
            return true
        }
        return false
    }

    override fun removeItem(itemBox: IItemBox, laneNum: Int, simulated: Boolean): Boolean = extract(itemBox, laneNum, simulated)

    override fun extract(itemBox: IItemBox, laneNum: Int, simulated: Boolean): Boolean {
        val lane = getLane(laneNum)
        if (lane.getItemBoxes().contains(itemBox)) {
            if (!simulated) {
                lane.getItemBoxes().remove(itemBox)
                lane.setHitBoxSpace(itemBox.getPosition(), false)
            }
            return true
        }
        return false
    }

    override fun getParent() = this

    override fun getLane(laneNum: Int): IConveyorBeltLane = this.lanes[laneNum]

    override fun getLaneCount() = lanes.size

    override fun getDir(): EnumFacing {
        return this.getBlockState().getValue(PROPERTY_DIRECTION)
    }

    override fun getSlope(): Int {
        if (world.getTileEntity(BlockPos(this.getPos().x + this.getDir().frontOffsetX, this.getPos().y + 1, this.getPos().z + this.getDir().frontOffsetZ)) != null)
            return 1
        else if (world.getTileEntity(BlockPos(this.getPos().x + this.getDir().opposite.frontOffsetX, this.getPos().y + 1, this.getPos().z + this.getDir().opposite.frontOffsetZ)) != null)
            return -1
        else
            return 0
    }
}