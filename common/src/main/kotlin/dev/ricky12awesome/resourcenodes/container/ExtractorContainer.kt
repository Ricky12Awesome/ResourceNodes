package dev.ricky12awesome.resourcenodes.container

import net.minecraft.core.NonNullList
import net.minecraft.world.Container
import net.minecraft.world.ContainerHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class SingleSlotItemList : NonNullList<ItemStack>(mutableListOf(ItemStack.EMPTY), ItemStack.EMPTY)
interface ExtractorContainer : Container {
  val items: SingleSlotItemList

  override fun getContainerSize(): Int = 1

  override fun getMaxStackSize(): Int = items[0].maxStackSize

  override fun isEmpty(): Boolean = items[0].isEmpty

  override fun getItem(slot: Int): ItemStack = when (slot) {
    0 -> items[0]
    else -> ItemStack.EMPTY
  }

  override fun removeItem(slot: Int, amount: Int): ItemStack {
    val result = ContainerHelper.removeItem(items, slot, amount)

    if (!result.isEmpty) {
      setChanged()
    }

    return result
  }

  override fun setItem(slot: Int, item: ItemStack) {
    items[0] = item

    if (item.count > item.maxStackSize) {
      item.count = item.maxStackSize;
    }
  }

  override fun clearContent() {
    items[0] = ItemStack.EMPTY
  }

  override fun removeItemNoUpdate(slot: Int): ItemStack {
    return ContainerHelper.takeItem(items, slot)
  }

  override fun canPlaceItem(i: Int, itemStack: ItemStack): Boolean {
    return false
  }

  override fun stillValid(player: Player): Boolean {
    return true
  }

  companion object {
    private class EmptyExtractorContainer : ExtractorContainer {
      override val items = SingleSlotItemList()
      override fun setChanged() {}
    }

    fun empty(): ExtractorContainer = EmptyExtractorContainer()
  }
}