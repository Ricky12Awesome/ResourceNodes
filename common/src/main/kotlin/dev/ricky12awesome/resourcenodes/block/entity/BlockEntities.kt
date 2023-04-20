package dev.ricky12awesome.resourcenodes.block.entity

import com.mojang.datafixers.types.constant.EmptyPart
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import dev.ricky12awesome.resourcenodes.MOD_ID
import dev.ricky12awesome.resourcenodes.block.Blocks
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntityType

object BlockEntities {
  val BLOCK_ENTITIES: DeferredRegister<BlockEntityType<*>> =
    DeferredRegister.create(MOD_ID, Registries.BLOCK_ENTITY_TYPE)

  val EXTRACTOR_BLOCK_ENTITY: RegistrySupplier<BlockEntityType<*>> =
    BLOCK_ENTITIES.register("extractor") {
      BlockEntityType.Builder.of(
        BlockEntityType.BlockEntitySupplier(::ExtractorBlockEntity),
        Blocks.EXTRACTOR_BLOCK.get()
      ).build(EmptyPart())
    }

  fun register() {
    BLOCK_ENTITIES.register()
  }
}

