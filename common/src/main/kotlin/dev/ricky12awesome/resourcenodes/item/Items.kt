package dev.ricky12awesome.resourcenodes.item

import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import dev.ricky12awesome.resourcenodes.MOD_ID
import dev.ricky12awesome.resourcenodes.tab.Tabs
import dev.ricky12awesome.resourcenodes.tab.tab
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item

@Suppress("MemberVisibilityCanBePrivate")
object Items {
  val ITEMS: DeferredRegister<Item> = DeferredRegister.create(MOD_ID, Registries.ITEM)

  val EXAMPLE_ITEM: RegistrySupplier<Item> = ITEMS.register("example_item") {
    Item(Item.Properties().tab(Tabs.TAB))
  }

  fun register() {
    ITEMS.register()
  }
}