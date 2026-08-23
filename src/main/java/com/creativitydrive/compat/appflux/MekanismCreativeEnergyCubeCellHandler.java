package com.creativitydrive.compat.appflux;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import com.creativitydrive.ae2.MekanismCreativeFluidTankStorageCell;
import mekanism.common.item.block.ItemBlockEnergyCube;
import mekanism.common.tier.EnergyCubeTier;
import net.minecraft.world.item.ItemStack;

public class MekanismCreativeEnergyCubeCellHandler implements ICellHandler {
    public static final MekanismCreativeEnergyCubeCellHandler INSTANCE = new MekanismCreativeEnergyCubeCellHandler();

    private MekanismCreativeEnergyCubeCellHandler() {
    }

    @Override
    public boolean isCell(ItemStack stack) {return isCreativeEnergyCell(stack);}

    @Override
    public StorageCell getCellInventory(ItemStack stack, ISaveProvider saveProvider) {
        if (!isCreativeEnergyCell(stack)) {
            return null;
        }
        return  new MekanismCreativeEnergyCubeStorageCell(stack);
    }

    private static boolean isCreativeEnergyCell(ItemStack stack) {
        return !stack.isEmpty()
                && stack.getItem() instanceof ItemBlockEnergyCube energyCube
                && energyCube.getTier() == EnergyCubeTier.CREATIVE;
    }
}
