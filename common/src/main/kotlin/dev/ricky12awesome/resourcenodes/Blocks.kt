package dev.ricky12awesome.resourcenodes

import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import dev.ricky12awesome.resourcenodes.tab.Tabs
import dev.ricky12awesome.resourcenodes.tab.tab
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material

object Blocks {
  val BLOCKS: DeferredRegister<Block> = DeferredRegister.create(MOD_ID, Registries.BLOCK)
  val BLOCK_ITEMS: DeferredRegister<Item> = DeferredRegister.create(MOD_ID, Registries.ITEM)

  val TEST_BLOCK: RegistrySupplier<Block> = BLOCKS.register("test_block") {
    Block(BlockBehaviour.Properties.of(Material.AMETHYST))
  }

  val TEST_BLOCK_ITEM: RegistrySupplier<Item>  = BLOCK_ITEMS.register("test_block") {
    BlockItem(TEST_BLOCK.get(), Item.Properties().tab(Tabs.TAB))
  }

  fun register() {
    BLOCKS.register()
    BLOCK_ITEMS.register()
  }
}