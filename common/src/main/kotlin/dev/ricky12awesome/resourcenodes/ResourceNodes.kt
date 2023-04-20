package dev.ricky12awesome.resourcenodes

import com.mojang.logging.LogUtils.getLogger
import dev.ricky12awesome.resourcenodes.block.Blocks
import dev.ricky12awesome.resourcenodes.item.Items
import org.slf4j.Logger

const val MOD_ID = "resource_nodes"

object ResourceNodes {
  val logger: Logger = getLogger()

  fun init() {
    Items.register()
    Blocks.register()

    logger.info(ResourceNodesPlatform.getConfigDirectory().toAbsolutePath().normalize().toString())
  }
}