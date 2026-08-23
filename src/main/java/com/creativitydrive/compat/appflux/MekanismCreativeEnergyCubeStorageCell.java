package com.creativitydrive.compat.appflux;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.StorageCell;
import com.glodblock.github.appflux.common.me.key.FluxKey;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class MekanismCreativeEnergyCubeStorageCell implements StorageCell {
    private static final long DISPLAYED_AMOUNT = Long.MAX_VALUE / 4;

    private final Component description;
    private final AEKey storedKey;

    public MekanismCreativeEnergyCubeStorageCell(ItemStack stack) {
        this.description = stack.getHoverName();

        // Applied FluxのFE用AEKeyを作る
        this.storedKey = FluxKey.of(EnergyType.FE);
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        MEStorage.checkPreconditions(what, amount, mode, source);

        if (!storedKey.equals(what)) {
            return 0;
        }

        return amount;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        MEStorage.checkPreconditions(what, amount, mode, source);

        if (!storedKey.equals(what)) {
            return 0;
        }

        return amount;
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        out.add(storedKey, DISPLAYED_AMOUNT);
    }

    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        return storedKey.equals(what);
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
}
