package com.cout970.magneticraft.api.conveyor

/**
 * Created by Qwyll on 10/29/2016.
 */
interface IHitBoxArray {

    fun size(): Int

    fun isOccupied(pos: Int): Boolean

    fun setOccupied(pos: Int, occupied: Boolean)

    fun clear()
}