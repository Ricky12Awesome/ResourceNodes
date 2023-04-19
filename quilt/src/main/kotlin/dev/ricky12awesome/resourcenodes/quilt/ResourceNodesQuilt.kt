package dev.ricky12awesome.resourcenodes.quilt

import dev.ricky12awesome.resourcenodes.ResourceNodes
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer

class ResourceNodesQuilt : ModInitializer {
  override fun onInitialize(mod: ModContainer?) {
    ResourceNodes.init()
  }
}