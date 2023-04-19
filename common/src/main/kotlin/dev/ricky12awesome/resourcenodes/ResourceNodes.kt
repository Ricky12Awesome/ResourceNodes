package dev.ricky12awesome.resourcenodes

import com.mojang.logging.LogUtils.getLogger
import org.slf4j.Logger

const val MOD_ID = "resource_nodes"

object ResourceNodes {
  val logger: Logger = getLogger()

  fun init() {
    println(ResourceNodesPlatform.getConfigDirectory().toAbsolutePath().normalize().toString())
  }
}