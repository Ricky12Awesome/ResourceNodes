package dev.ricky12awesome.resourcenodes.block.entity

import dev.ricky12awesome.resourcenodes.container.ExtractorContainer
import dev.ricky12awesome.resourcenodes.container.SingleSlotItemList
import earth.terrarium.botarium.common.energy.base.EnergyAttachment
import earth.terrarium.botarium.common.energy.impl.SimpleEnergyContainer
import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.ContainerHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState


class ExtractorBlockEntity(pos: BlockPos, state: BlockState) :
  BlockEntity(BlockEntities.EXTRACTOR_BLOCK_ENTITY.get(), pos, state),
  ExtractorContainer, EnergyAttachment.Block {

  private var energyContainer: WrappedBlockEnergyContainer? = null

  override val items = SingleSlotItemList()

  var resource = ItemStack.EMPTY
  var amountPerSecond = 0

  override fun saveAdditional(tag: CompoundTag) {
    ContainerHelper.saveAllItems(tag, items)

    tag.put("Resource", resource.save(CompoundTag()))
    tag.put("AmountPerSecond", IntTag.valueOf(amountPerSecond))

    super.saveAdditional(tag)
  }

  override fun load(compoundTag: CompoundTag) {
    super.load(compoundTag)
    ContainerHelper.loadAllItems(compoundTag, items)
    resource = ItemStack.of(compoundTag.getCompound("Resource"))
    amountPerSecond = compoundTag.getInt("AmountPerSecond")
  }

  override fun getUpdatePacket(): Packet<ClientGamePacketListener>? {
    return ClientboundBlockEntityDataPacket.create(this)
  }

  override fun getUpdateTag(): CompoundTag {
    return saveWithFullMetadata()
  }

  fun generateResource() {
    if (isEmpty) {
      setItem(0, resource.copyWithCount(1))
    } else {
      if (items[0].count >= items[0].maxStackSize) {
        return
      }

      items[0].count += 1
    }
  }

  fun tick(level: Level, pos: BlockPos, state: BlockState) {
    if ((energyContainer?.storedEnergy ?: 0L) < 50L) {
      return
    }

    energyContainer?.extractEnergy(50L, false)

    if (level.gameTime % 10L == 0L) {
      generateResource()
    }
  }

  override fun getEnergyStorage(holder: BlockEntity?): WrappedBlockEnergyContainer {
    return energyContainer ?: WrappedBlockEnergyContainer(this, SimpleEnergyContainer(1000000))
      .also { energyContainer = it }
  }
}