package dev.ricky12awesome.resourcenodes.tag

import dev.ricky12awesome.resourcenodes.MOD_ID
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object Tags {
  val VALID_NODES: TagKey<Block> = TagKey.create(Registries.BLOCK, ResourceLocation(MOD_ID, "valid_nodes"))
}