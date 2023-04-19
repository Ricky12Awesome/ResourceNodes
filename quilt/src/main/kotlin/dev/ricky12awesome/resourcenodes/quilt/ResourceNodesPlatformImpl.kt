package dev.ricky12awesome.resourcenodes.quilt

import org.quiltmc.loader.api.QuiltLoader
import java.nio.file.Path

object ResourceNodesPlatformImpl {
  @JvmStatic
  fun getConfigDirectory(): Path = QuiltLoader.getConfigDir()
}