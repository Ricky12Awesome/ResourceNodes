@file:Suppress("OVERRIDE_DEPRECATION")

package dev.ricky12awesome.resourcenodes.block

import dev.ricky12awesome.resourcenodes.ResourceNodes
import dev.ricky12awesome.resourcenodes.block.entity.BlockEntities
import dev.ricky12awesome.resourcenodes.block.entity.ExtractorBlockEntity
import dev.ricky12awesome.resourcenodes.tag.Tags
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.TagParser
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.level.block.Blocks as MCBlocks

class ExtractorBlock : Block(Properties.of(Material.METAL)), EntityBlock {

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): ExtractorBlockEntity {
    return ExtractorBlockEntity(blockPos, blockState)
  }

  override fun <T : BlockEntity?> getTicker(
    level: Level,
    blockState: BlockState,
    blockEntityType: BlockEntityType<T>
  ): BlockEntityTicker<T>? {
    if (blockEntityType != BlockEntities.EXTRACTOR_BLOCK_ENTITY.get()) {
      return null
    }

    return BlockEntityTicker { level, pos, state, entity ->
      (entity as ExtractorBlockEntity).tick(level, pos, state)
    }
  }

  override fun use(
    blockState: BlockState,
    level: Level,
    blockPos: BlockPos,
    player: Player,
    interactionHand: InteractionHand,
    blockHitResult: BlockHitResult
  ): InteractionResult {
    if (level.isClientSide) return InteractionResult.SUCCESS

    val entity = level.getBlockEntity(blockPos) as ExtractorBlockEntity

    player.displayClientMessage(
      Component
        .empty()
        .append(Component.literal("T/R: ").withStyle(ChatFormatting.LIGHT_PURPLE))
        .append(Component.literal("${entity.ticksPerResource}").withStyle(ChatFormatting.RED))
        .append(Component.literal(" RF/T: ").withStyle(ChatFormatting.LIGHT_PURPLE))
        .append(Component.literal("${entity.rfPerTick}").withStyle(ChatFormatting.RED))
        .append(Component.literal(" Res: ").withStyle(ChatFormatting.LIGHT_PURPLE))
        .append(Component.translatable(entity.resource.descriptionId).withStyle(ChatFormatting.AQUA))
        .append(Component.literal(" Cnt: ").withStyle(ChatFormatting.LIGHT_PURPLE))
        .append(Component.literal("${entity.items[0].count}").withStyle(ChatFormatting.RED)), true
    )

    return InteractionResult.SUCCESS
  }

  override fun onPlace(
    blockState: BlockState,
    level: Level,
    blockPos: BlockPos,
    blockState2: BlockState,
    bl: Boolean
  ) {
    val entity = level.getBlockEntity(blockPos) as? ExtractorBlockEntity ?: return
    val node = blockPos
      .adjacent()
      .map(level::getBlockState)
      .firstOrNull { it.isInTag(Tags.VALID_NODES) }
      ?: return

    val block = node.block.registryName()?.toString() ?: return
    val config = ResourceNodes.config.getExtractorBlockConfig(block) ?: return
    val resourceId = ResourceLocation(config.resource.id)
    val resourceItem = BuiltInRegistries.ITEM.get(resourceId)
    val resource = ItemStack(resourceItem, 1)

    resource.tag = try {
      config.resource.nbt?.let(TagParser::parseTag)
    } catch (e: Exception) {
      null
    }

    entity.node = node.block.registryName()
    entity.resource = resource
    entity.capacity = config.rfCapacity
    entity.rfPerTick = config.rfPerTick
    entity.ticksPerResource = config.ticksPerResource
  }

  override fun getDrops(blockState: BlockState, builder: LootContext.Builder): MutableList<ItemStack> {
    return mutableListOf(
      ItemStack(Blocks.EXTRACTOR_BLOCK_ITEM.get(), 1)
    )
  }

  override fun updateShape(
    blockState: BlockState,
    direction: Direction,
    blockState2: BlockState,
    levelAccessor: LevelAccessor,
    blockPos: BlockPos,
    blockPos2: BlockPos
  ): BlockState {
    val entity = levelAccessor.getBlockEntity(blockPos) as? ExtractorBlockEntity
    val node = entity?.node
    val result = blockPos
      .adjacent()
      .map(levelAccessor::getBlockState)
      .map { it.block.registryName() }
      .any { name == node }

    if (!result) {
      return MCBlocks.AIR.defaultBlockState()
    }

    return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2)
  }
}