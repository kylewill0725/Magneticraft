package com.cout970.magneticraft.world

import com.cout970.magneticraft.block.BlockLimestone
import com.cout970.magneticraft.block.BlockOre
import com.cout970.magneticraft.block.states.BlockLimestoneStates
import com.cout970.magneticraft.block.states.BlockOreStates
import com.cout970.magneticraft.block.states.BlockProperties
import com.cout970.magneticraft.config.Config
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkGenerator
import net.minecraft.world.chunk.IChunkProvider
import net.minecraftforge.fml.common.IWorldGenerator
import java.util.*

/**
 * Created by cout970 on 15/05/2016.
 */
object WorldGenerator : IWorldGenerator {

    val generators = mutableListOf<IWorldGenerator>()

    fun init(){
        generators.add(OreGenerator(BlockOre.defaultState.withProperty(BlockProperties.blockOreState, BlockOreStates.COPPER),   Config.copperOre))
        generators.add(OreGenerator(BlockOre.defaultState.withProperty(BlockProperties.blockOreState, BlockOreStates.LEAD),   Config.leadOre))
        generators.add(OreGenerator(BlockOre.defaultState.withProperty(BlockProperties.blockOreState, BlockOreStates.COBALT),   Config.cobaltOre))
        generators.add(OreGenerator(BlockOre.defaultState.withProperty(BlockProperties.blockOreState, BlockOreStates.TUNGSTEN),   Config.tungstenOre))
        generators.add(GaussianOreGenerator(BlockLimestone.defaultState.withProperty(BlockProperties.blockLimestoneState, BlockLimestoneStates.NORMAL), Config.limestone))
    }

    override fun generate(random: Random?, chunkX: Int, chunkZ: Int, world: World?, chunkGenerator: IChunkGenerator?, chunkProvider: IChunkProvider?) {
        if(world == null || random == null)return
        for(i in generators){
            i.generate(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider)
        }
    }
}