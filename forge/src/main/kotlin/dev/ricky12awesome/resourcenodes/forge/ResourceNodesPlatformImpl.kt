package dev.ricky12awesome.resourcenodes.forge

import net.minecraftforge.fml.loading.FMLPaths
import java.nio.file.Path

@Suppress("unused")
object ResourceNodesPlatformImpl {
  @JvmStatic
  fun getConfigDirectory(): Path = FMLPaths.CONFIGDIR.get()
}