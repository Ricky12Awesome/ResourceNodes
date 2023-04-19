package dev.ricky12awesome.resourcenodes.forge


import dev.architectury.platform.forge.EventBuses
import dev.ricky12awesome.resourcenodes.MOD_ID
import dev.ricky12awesome.resourcenodes.ResourceNodes
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

@Mod(MOD_ID)
class ResourceNodesForge {
  init {
    // Submit our event bus to let architectury register our content on the right time
    EventBuses.registerModEventBus(MOD_ID, FMLJavaModLoadingContext.get().modEventBus)
    ResourceNodes.init()
  }
}