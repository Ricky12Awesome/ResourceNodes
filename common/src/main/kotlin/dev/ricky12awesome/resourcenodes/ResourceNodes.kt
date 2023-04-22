package dev.ricky12awesome.resourcenodes

import com.mojang.logging.LogUtils.getLogger
import dev.ricky12awesome.resourcenodes.block.Blocks
import dev.ricky12awesome.resourcenodes.config.ResourceNodesConfig
import dev.ricky12awesome.resourcenodes.item.Items
import org.slf4j.Logger
import kotlin.io.path.absolute

const val MOD_ID = "resource_nodes"

object ResourceNodes {
  val logger: Logger = getLogger()

  lateinit var config: ResourceNodesConfig
    private set

  fun reloadConfig() {
    config = ResourceNodesConfig.initialize(
      ResourceNodesPlatform
        .getConfigDirectory()
        .absolute()
        .resolve("$MOD_ID.json5")
    )
  }

  fun init() {
    reloadConfig()

    Items.register()
    Blocks.register()


//    logger.info(ResourceNodesPlatform.getConfigDirectory().toAbsolutePath().normalize().toString())
  }
}