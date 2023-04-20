package dev.ricky12awesome.resourcenodes.block

import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import dev.ricky12awesome.resourcenodes.MOD_ID
import dev.ricky12awesome.resourcenodes.block.entity.BlockEntities
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object Blocks {
  val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(MOD_ID, Registries.BLOCK)
  val BLOCK_ITEMS: DeferredRegister<Item> = DeferredRegister.create(MOD_ID, Registries.ITEM)

  val EXTRACTOR_BLOCK: RegistrySupplier<Block> = BLOCKS.register("extractor_block", ::ExtractorBlock)
  val EXTRACTOR_BLOCK_ITEM: RegistrySupplier<Item> = BLOCK_ITEMS.register("extractor_block", ::ExtractorBlockItem)

  fun register() {
    BLOCKS.register()
    BLOCK_ITEMS.register()
    BlockEntities.register()
  }
}

