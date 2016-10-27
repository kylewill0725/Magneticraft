package com.cout970.magneticraft.block

import coffee.cypher.mcextlib.extensions.aabb.to
import com.cout970.magneticraft.tileentity.TileConveyor
import com.cout970.magneticraft.util.get
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

/**
* Created by Qwyll on 10/2/2016.
*/
object BlockConveyor : BlockMultiState( Material.IRON, "conveyor" ), ITileEntityProvider {
    val boundingBox = Vec3d.ZERO to Vec3d(1.0, 0.25, 1.0)

    override fun isFullBlock(state: IBlockState?) = false
    override fun isOpaqueCube(state: IBlockState?) = false
    override fun isFullCube(state: IBlockState?) = false
    override fun isVisuallyOpaque() = false
    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?) = boundingBox

    override fun getStateFromMeta(meta: Int): IBlockState = defaultState.withProperty(PROPERTY_FACING, EnumFacing.getHorizontal(meta))
    override fun createBlockState(): BlockStateContainer = BlockStateContainer(this, PROPERTY_FACING)
    override fun getMetaFromState(state: IBlockState): Int = PROPERTY_FACING[state].ordinal
    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity = TileConveyor()

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos?, state: IBlockState?, placer: EntityLivingBase?, stack: ItemStack?) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack)
        worldIn.setBlockState(pos, defaultState.withProperty(PROPERTY_FACING, placer?.horizontalFacing))
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand?, heldItem: ItemStack?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
//        if (playerIn.isSneaking) return false
//        val tileEntity = worldIn.getTileEntity(pos)
//        if (tileEntity is TileConveyor) {
//            tileEntity
//        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ)
    }
}