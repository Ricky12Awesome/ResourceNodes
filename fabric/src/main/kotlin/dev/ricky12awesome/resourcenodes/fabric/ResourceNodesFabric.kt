package dev.ricky12awesome.resourcenodes.fabric

import dev.ricky12awesome.resourcenodes.ResourceNodes
import net.fabricmc.api.ModInitializer

class ResourceNodesFabric : ModInitializer {
  override fun onInitialize() {
    ResourceNodes.init()
  }
}