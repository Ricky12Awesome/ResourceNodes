package dev.ricky12awesome.resourcenodes.config

import io.github.xn32.json5k.Json5
import io.github.xn32.json5k.SerialComment
import io.github.xn32.json5k.decodeFromStream
import io.github.xn32.json5k.encodeToStream
import kotlinx.serialization.Serializable
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

@Serializable
data class ResourceNodesConfig(
  @SerialComment(
    """
    {
      // What node is this config for?
      // this can be any block
      "node": "resource_nodes:node_dirt"
      
      // How much rf capacity should this have?
      "rfCapacity": 10000,
      
      // How much rf per tick should this take?
      "rfPerTick": 50,
      
      // How often should this generate a resource? 
      // in ticks, there's 20 ticks in a second
      // so 100 ticks would be 5 seconds
      "ticksPerResource": 100,
      
      // What resource should this generate?
      "resource": {
        // ID of the item to generate
        "id": "minecraft:dirt",
        
        // Optional NBT data
        "nbt": "{}"
      }
    }
  """
  )
  val extractorBlockConfigs: List<ExtractorBlockConfig>
) {
  val extractorBlockConfigsAsMap = extractorBlockConfigs.associateBy(ExtractorBlockConfig::node)

  fun getExtractorBlockConfig(name: String): ExtractorBlockConfig? {
    return extractorBlockConfigsAsMap[name]
  }

  companion object {
    private val json5 = Json5 {
      prettyPrint = true
      quoteMemberNames = true
      indentationWidth = 2
      useSingleQuotes = false
      encodeDefaults = false
    }

    val default = ResourceNodesConfig(
      extractorBlockConfigs = listOf(
        ExtractorBlockConfig(
          node = "resource_nodes:node_coal",
          rfCapacity = 500,
          rfPerTick = 250,
          ticksPerResource = 100,
          resource = ExtractorBlockResourceConfig("minecraft:coal")
        ),
        ExtractorBlockConfig(
          node = "resource_nodes:node_copper",
          rfCapacity = 1000,
          rfPerTick = 500,
          ticksPerResource = 100,
          resource = ExtractorBlockResourceConfig("minecraft:raw_copper")
        ),
        ExtractorBlockConfig(
          node = "resource_nodes:node_iron",
          rfCapacity = 2500,
          rfPerTick = 1250,
          ticksPerResource = 200,
          resource = ExtractorBlockResourceConfig("minecraft:raw_iron")
        ),
        ExtractorBlockConfig(
          node = "resource_nodes:node_gold",
          rfCapacity = 5000,
          rfPerTick = 2500,
          ticksPerResource = 600,
          resource = ExtractorBlockResourceConfig("minecraft:raw_gold")
        ),
        ExtractorBlockConfig(
          node = "resource_nodes:node_diamond",
          rfCapacity = 10000,
          rfPerTick = 5000,
          ticksPerResource = 1200,
          resource = ExtractorBlockResourceConfig("minecraft:diamond")
        ),
        ExtractorBlockConfig(
          node = "resource_nodes:node_emerald",
          rfCapacity = 20000,
          rfPerTick = 10000,
          ticksPerResource = 1200,
          resource = ExtractorBlockResourceConfig("minecraft:emerald")
        ),
      )
    )

    fun initialize(path: Path): ResourceNodesConfig {
      return if (path.exists()) {
        json5.decodeFromStream(path.inputStream())
      } else {
        json5.encodeToStream(default, path.outputStream())
        default
      }
    }
  }
}

@Serializable
data class ExtractorBlockResourceConfig(
  val id: String,
  val nbt: String? = null
)

@Serializable
data class ExtractorBlockConfig(
  val node: String,
  val rfCapacity: Long,
  val rfPerTick: Long,
  val ticksPerResource: Long,
  val resource: ExtractorBlockResourceConfig = ExtractorBlockResourceConfig("minecraft:dirt"),
)