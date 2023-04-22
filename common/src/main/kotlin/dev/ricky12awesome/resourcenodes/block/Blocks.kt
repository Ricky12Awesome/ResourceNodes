package dev.ricky12awesome.resourcenodes.block

import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import dev.ricky12awesome.resourcenodes.MOD_ID
import dev.ricky12awesome.resourcenodes.block.entity.BlockEntities
import dev.ricky12awesome.resourcenodes.tab.Tabs
import dev.ricky12awesome.resourcenodes.tab.tab
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material

object Blocks {
  val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(MOD_ID, Registries.BLOCK)
  val BLOCK_ITEMS: DeferredRegister<Item> = DeferredRegister.create(MOD_ID, Registries.ITEM)

  val EXTRACTOR_BLOCK: RegistrySupplier<Block> = BLOCKS.register("extractor", ::ExtractorBlock)
  val EXTRACTOR_BLOCK_ITEM: RegistrySupplier<Item> = BLOCK_ITEMS.register("extractor", ::ExtractorBlockItem)

  val NODE_COAL_BLOCK: RegistrySupplier<Block> = nodeBlock("coal")
  val NODE_COAL_BLOCK_ITEM: RegistrySupplier<Item> = nodeBlockItem(NODE_COAL_BLOCK, "coal")

  val NODE_COPPER_BLOCK: RegistrySupplier<Block> = nodeBlock("copper")
  val NODE_COPPER_BLOCK_ITEM: RegistrySupplier<Item> = nodeBlockItem(NODE_COPPER_BLOCK, "copper")

  val NODE_IRON_BLOCK: RegistrySupplier<Block> = nodeBlock("iron")
  val NODE_IRON_BLOCK_ITEM: RegistrySupplier<Item> = nodeBlockItem(NODE_IRON_BLOCK, "iron")

  val NODE_GOLD_BLOCK: RegistrySupplier<Block> = nodeBlock("gold")
  val NODE_GOLD_BLOCK_ITEM: RegistrySupplier<Item> = nodeBlockItem(NODE_GOLD_BLOCK, "gold")

  val NODE_DIAMOND_BLOCK: RegistrySupplier<Block> = nodeBlock("diamond")
  val NODE_DIAMOND_BLOCK_ITEM: RegistrySupplier<Item> = nodeBlockItem(NODE_DIAMOND_BLOCK, "diamond")

  val NODE_EMERALD_BLOCK: RegistrySupplier<Block> = nodeBlock("emerald")
  val NODE_EMERALD_BLOCK_ITEM: RegistrySupplier<Item> = nodeBlockItem(NODE_EMERALD_BLOCK, "emerald")

  fun nodeBlock(name: String): RegistrySupplier<Block> {
    return BLOCKS.register("node_$name") {
      Block(BlockBehaviour.Properties.of(Material.METAL))
    }
  }

  fun nodeBlockItem(block: RegistrySupplier<Block>, name: String): RegistrySupplier<Item> {
    return BLOCK_ITEMS.register("node_$name") {
      BlockItem(block.get(), Item.Properties().tab(Tabs.TAB))
    }
  }

  fun register() {
    BLOCKS.register()
    BLOCK_ITEMS.register()
    BlockEntities.register()
  }
}

fun Block.registryName(): ResourceLocation? {
  return `arch$registryName`()
}

fun BlockState.isInTag(tag: TagKey<Block>): Boolean {
  return `is`(tag)
}

fun BlockPos.adjacent(): List<BlockPos> {
  return listOf(
    above(),
    below(),
    north(),
    south(),
    west(),
    east(),
  )
}