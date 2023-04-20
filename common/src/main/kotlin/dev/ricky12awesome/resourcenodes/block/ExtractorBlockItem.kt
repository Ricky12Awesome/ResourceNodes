package dev.ricky12awesome.resourcenodes.block

import dev.ricky12awesome.resourcenodes.tab.Tabs
import dev.ricky12awesome.resourcenodes.tab.tab
import net.minecraft.ChatFormatting
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.state.BlockState

class ExtractorBlockItem : BlockItem(Blocks.EXTRACTOR_BLOCK.get(), Properties().tab(Tabs.TAB)) {
  override fun canPlace(blockPlaceContext: BlockPlaceContext, blockState: BlockState): Boolean {
    val pos = blockPlaceContext.clickedPos.below()
    val block = blockPlaceContext.level.getBlockState(pos).block

    blockPlaceContext.player?.displayClientMessage(
      Component
        .empty()
        .append(Component.literal("Face: ").withStyle(ChatFormatting.LIGHT_PURPLE))
        .append(Component.literal("${blockPlaceContext.clickedFace}").withStyle(ChatFormatting.RED))
        .append(Component.literal(" Block: ").withStyle(ChatFormatting.LIGHT_PURPLE))
        .append(Component.translatable(block.descriptionId).withStyle(ChatFormatting.AQUA))
        .append(Component.literal(" Pos: ").withStyle(ChatFormatting.LIGHT_PURPLE))
        .append(Component.literal("$pos").withStyle(ChatFormatting.GREEN)),
      true
    )

    return blockPlaceContext.clickedFace == Direction.UP &&
        block == net.minecraft.world.level.block.Blocks.IRON_BLOCK &&
        super.canPlace(blockPlaceContext, blockState)
  }
}