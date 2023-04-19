package dev.ricky12awesome.resourcenodes

import dev.architectury.injectables.annotations.ExpectPlatform
import dev.architectury.platform.Platform
import java.nio.file.Path

object ResourceNodesPlatform {
  @ExpectPlatform
  @JvmStatic
  fun getConfigDirectory(): Path {
    // Just throw an error, the content should get replaced at runtime.
    throw AssertionError()
  }
}