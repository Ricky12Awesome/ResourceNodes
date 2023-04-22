package dev.ricky12awesome.resourcenodes.block

import dev.ricky12awesome.resourcenodes.tab.Tabs
import dev.ricky12awesome.resourcenodes.tab.tab
import dev.ricky12awesome.resourcenodes.tag.Tags
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.state.BlockState

class ExtractorBlockItem : BlockItem(Blocks.EXTRACTOR_BLOCK.get(), Properties().tab(Tabs.TAB)) {
  override fun canPlace(context: BlockPlaceContext, state: BlockState): Boolean {
    val result = context.clickedPos.adjacent().any {
      context.level.getBlockState(it).isInTag(Tags.VALID_NODES)
    }

    return result && super.canPlace(context, state)
  }
}