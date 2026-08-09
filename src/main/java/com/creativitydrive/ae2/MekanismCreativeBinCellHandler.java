package com.creativitydrive.ae2;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import mekanism.common.item.block.ItemBlockBin;
import mekanism.common.tier.BinTier;
import net.minecraft.world.item.ItemStack;

/** Treats a configured Mekanism creative bin as an AE2 infinite item cell. */
public final class MekanismCreativeBinCellHandler implements ICellHandler {
    public static final MekanismCreativeBinCellHandler INSTANCE = new MekanismCreativeBinCellHandler();

    private MekanismCreativeBinCellHandler() {
    }

    @Override
    public boolean isCell(ItemStack stack) {
        return isCreativeBin(stack);
    }

    @Override
    public StorageCell getCellInventory(ItemStack stack, ISaveProvider saveProvider) {
        if (!isCreativeBin(stack)) {
            return null;
        }
        return new MekanismCreativeBinStorageCell(stack);
    }

    private static boolean isCreativeBin(ItemStack stack) {
        return !stack.isEmpty()
                && stack.getItem() instanceof ItemBlockBin bin
                && bin.getTier() == BinTier.CREATIVE;
    }
}
