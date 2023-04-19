package dev.ricky12awesome.resourcenodes.tab

import dev.architectury.registry.CreativeTabRegistry
import dev.ricky12awesome.resourcenodes.MOD_ID
import dev.ricky12awesome.resourcenodes.item.Items
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.function.Supplier

object Tabs {
  val TAB: CreativeTabRegistry.TabSupplier = CreativeTabRegistry.create(
    ResourceLocation(MOD_ID, "tab"),
    Supplier {
      ItemStack(Items.EXAMPLE_ITEM.get())
    }
  )
}

@Suppress("UnstableApiUsage")
fun Item.Properties.tab(tab: CreativeTabRegistry.TabSupplier): Item.Properties {
  return `arch$tab`(tab)
}

@Suppress("UnstableApiUsage")
fun Item.Properties.tab(tab: CreativeModeTab): Item.Properties {
  return `arch$tab`(tab)
}