package com.creativitydrive.ae2;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.StorageCell;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class MekanismCreativeChemicalTankStorageCell implements StorageCell {
    private static final long DISPLAYED_AMOUNT = Long.MAX_VALUE / 4;

    private final Component description;
    private final AEKey storedKey;

    public MekanismCreativeChemicalTankStorageCell(ItemStack stack) {
        this.description = stack.getHoverName();
        MekanismKey chemicalKey = getStoredChemicalKey(stack);
        // An empty creative chemical tank is itself the infinitely available item.
        this.storedKey = chemicalKey != null ? chemicalKey : AEItemKey.of(stack);
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

    private static MekanismKey getStoredChemicalKey(ItemStack stack) {
        IChemicalHandler handler = stack.getCapability(Capabilities.CHEMICAL.item());
        if (handler == null) {
            return null;
        }
        for (int tank = 0; tank < handler.getChemicalTanks(); tank++) {
            ChemicalStack chemical = handler.getChemicalInTank(tank);
            if (!chemical.isEmpty()) {
                return MekanismKey.of(chemical);
            }
        }
        return null;
    }
}
