package com.cout970.magneticraft.api.conveyor

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos

/**
* Created by Qwyll on 10/2/2016.
*/
interface IConveyorBelt {
    enum class BeltInteraction {
        DIRECT, INVERSE, LEFT_T, RIGHT_T, NOTHING;

        companion object {
            fun InterBelt(directionA: EnumFacing, directionB: EnumFacing): BeltInteraction {
                if (directionA == directionB) return DIRECT
                if (directionA == directionB.opposite) return INVERSE
                if (directionA == directionB.rotateYCCW()) return RIGHT_T
                if (directionA == directionB.rotateY()) return LEFT_T
                return NOTHING
            }
        }
    }

    fun addItem( direction: EnumFacing, pos: BlockPos, itemBox: IItemBox )
    fun removeItem( itemBox: IItemBox, laneNum: Int, simulated: Boolean )
    fun getLaneCount(): Int
    fun getLane(laneNum: Int ): IConveyorBeltLane
    fun getDir(): EnumFacing
    fun getSlope(): Int
    fun extract( itemBox: IItemBox, laneNum: Int, simulated: Boolean ): Boolean
    fun inject( pos: BlockPos, itemBox: IItemBox, laneNum: Int, simulated: Boolean ): Boolean
    fun getParent(): TileEntity
    fun onChange()
}