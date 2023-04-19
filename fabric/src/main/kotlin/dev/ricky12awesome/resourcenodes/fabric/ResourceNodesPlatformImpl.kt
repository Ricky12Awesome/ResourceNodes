package dev.ricky12awesome.resourcenodes.fabric

import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path

object ResourceNodesPlatformImpl {
  @JvmStatic
  fun getConfigDirectory(): Path = FabricLoader.getInstance().configDir
}