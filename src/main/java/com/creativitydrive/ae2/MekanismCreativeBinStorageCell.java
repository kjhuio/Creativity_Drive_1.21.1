package com.creativitydrive.ae2;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.StorageCell;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import mekanism.common.attachments.containers.item.ComponentBackedBinInventorySlot;
import mekanism.common.inventory.slot.BinInventorySlot;

import org.slf4j.Logger;

import static com.creativitydrive.Config.ALLOW_SELF_REPLICATION;

/**
 * A read-through view of the item configured in a creative bin. An empty bin
 * supplies itself so it can be duplicated through the ME network.
 */
public final class MekanismCreativeBinStorageCell implements StorageCell {
    private static final long DISPLAYED_AMOUNT = Long.MAX_VALUE / 4;
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Component description;
    private final AEItemKey itemKey;

    public MekanismCreativeBinStorageCell(ItemStack stack) {
        this.description = stack.getHoverName();
        AEItemKey storedItemKey = getStoredItemKey(stack);
        // An empty creative bin is itself the infinitely available item.
        if (storedItemKey != null) {
            this.itemKey = storedItemKey;
        } else if (ALLOW_SELF_REPLICATION.get()) {
            this.itemKey = AEItemKey.of(stack);
            LOGGER.warn("{} Self-replication started",stack.getHoverName().getString());
        } else {
            this.itemKey = null;
        }
    }


    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        LOGGER.debug("insert called");

        MEStorage.checkPreconditions(what, amount, mode, source);
        boolean accepted = itemKey != null && itemKey.equals(what);

        if (accepted) {
            if (source.player().isPresent()) {
                Player player = source.player().get();
                String playerName = player.getName().getString();
                LOGGER.debug("Insert creative bin item:{} x{} by{} ",what,amount,playerName);
            }
        }

        return accepted ? amount : 0;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        MEStorage.checkPreconditions(what, amount, mode, source);
        boolean accepted = itemKey != null && itemKey.equals(what);

        if (accepted) {
            if (source.player().isPresent()) {
                Player player = source.player().get();
                String playerName = player.getName().getString();
                LOGGER.debug("Extract creative bin item:{} x{} by{} ",what,amount,playerName);
            }
        }

        return accepted ? amount : 0;
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        if (itemKey != null) {
            out.add(itemKey, DISPLAYED_AMOUNT);
        }
    }

    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        return itemKey != null && itemKey.equals(what);
    }

    @Override
    public CellState getStatus() {
        return CellState.TYPES_FULL;
    }

    @Override
    public double getIdleDrain() {
        return 0;
    }

    @Override
    public boolean canFitInsideCell() {
        return false;
    }

    @Override
    public Component getDescription() {
        return description;
    }

    @Override
    public void persist() {
    }

    private static AEItemKey getStoredItemKey(ItemStack stack) {
        ComponentBackedBinInventorySlot inventory = BinInventorySlot.getForStack(stack);
        if (inventory == null) {
            return null;
        }

        ItemStack stored = inventory.getStack();
        return stored.isEmpty() ? null : AEItemKey.of(stored);
    }
}
