package com.cout970.magneticraft.api.conveyor.premade

import com.cout970.magneticraft.api.conveyor.IHitBoxArray
import java.util.*

/**
 * Created by Qwyll on 10/29/2016.
 */
class HitBoxArray : IHitBoxArray {
    private var spaces = BooleanArray(16)

    override fun size() = this.spaces.size

    override fun isOccupied(pos: Int) = this.spaces[pos]

    override fun setOccupied(pos: Int, occupied: Boolean) {
        this.spaces[pos] = occupied
    }

    override fun clear() {
        Arrays.fill(this.spaces, false)
    }
}