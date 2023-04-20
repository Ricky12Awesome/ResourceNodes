package dev.ricky12awesome.resourcenodes.block

import dev.ricky12awesome.resourcenodes.block.entity.BlockEntities
import dev.ricky12awesome.resourcenodes.block.entity.ExtractorBlockEntity
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.phys.BlockHitResult

class ExtractorBlock : Block(Properties.of(Material.METAL)), EntityBlock {

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): ExtractorBlockEntity {
    return ExtractorBlockEntity(blockPos, blockState)
  }

  override fun <T : BlockEntity?> getTicker(
    ignoreLevel: Level,
    ignoreBlockState: BlockState,
    blockEntityType: BlockEntityType<T>
  ): BlockEntityTicker<T>? {
    if (blockEntityType != BlockEntities.EXTRACTOR_BLOCK_ENTITY.get()) {
      return null
    }

    return BlockEntityTicker { level, pos, state, entity ->
      (entity as ExtractorBlockEntity).tick(level, pos, state)
    }
  }

  @Deprecated("idk why this is deprecated")
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

    if (entity.resource.isEmpty) {
      entity.resource = ItemStack(Items.RAW_IRON, 64)
    }

    player.displayClientMessage(
      Component
        .empty()
        .append(Component.literal("APS: ").withStyle(ChatFormatting.LIGHT_PURPLE))
        .append(Component.literal("${entity.amountPerSecond}").withStyle(ChatFormatting.RED))
        .append(Component.literal(" Inv: ").withStyle(ChatFormatting.LIGHT_PURPLE))
        .append(Component.translatable(entity.items[0].descriptionId).withStyle(ChatFormatting.AQUA))
        .append(Component.literal(" Res: ").withStyle(ChatFormatting.LIGHT_PURPLE))
        .append(Component.translatable(entity.resource.descriptionId).withStyle(ChatFormatting.AQUA))
        .append(Component.literal(" Cnt: ").withStyle(ChatFormatting.LIGHT_PURPLE))
        .append(Component.literal("${entity.items[0].count}").withStyle(ChatFormatting.RED)), true
    )

    return InteractionResult.SUCCESS
  }
}