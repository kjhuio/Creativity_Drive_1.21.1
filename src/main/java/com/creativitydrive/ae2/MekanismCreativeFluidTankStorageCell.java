package com.creativitydrive.ae2;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.StorageCell;
import com.creativitydrive.Config;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import org.slf4j.Logger;

public final class MekanismCreativeFluidTankStorageCell implements StorageCell {
    private static final long DISPLAYED_AMOUNT = Long.MAX_VALUE / 4;
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Component description;
    private final AEKey storedKey;

    public MekanismCreativeFluidTankStorageCell(ItemStack stack) {
        this.description = stack.getHoverName();
        AEFluidKey fluidKey = getStoredFluidKey(stack);
        // An empty creative tank is itself the infinitely available item.
        if (fluidKey != null) {
            this.storedKey = fluidKey;
        } else if (Config.ALLOW_SELF_REPLICATION.get()) {
            this.storedKey = AEItemKey.of(stack);
            LOGGER.warn("{} Self-replication started",stack.getHoverName().getString());
        } else {
            this.storedKey = null;
        }
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        LOGGER.debug("insert() called");
        MEStorage.checkPreconditions(what, amount, mode, source);
        if (!storedKey.equals(what)) {
            return 0;
        }
        return amount;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        MEStorage.checkPreconditions(what, amount, mode, source);
        boolean accepted = storedKey != null && storedKey.equals(what);

        if (accepted) {
            if (source.player().isPresent()) {
                Player player = source.player().get();
                String playerName = player.getName().getString();
                LOGGER.info("Insert creative fluid tank item:{} x{} by{} ",what,amount,playerName);
            }
        }

        return accepted ? amount : 0;
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

    private static AEFluidKey getStoredFluidKey(ItemStack stack) {
        var handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (handler == null) {
            return null;
        }

        for (int tank = 0; tank < handler.getTanks(); tank++) {
            FluidStack fluid = handler.getFluidInTank(tank);
            if (!fluid.isEmpty()) {
                return AEFluidKey.of(fluid);
            }
        }
        return null;
    }
}
