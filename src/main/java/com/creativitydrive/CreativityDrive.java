package com.creativitydrive;

import appeng.api.storage.StorageCells;
import com.creativitydrive.ae2.MekanismCreativeChemicalTankCellHandler;
import com.creativitydrive.ae2.MekanismCreativeBinCellHandler;
import com.creativitydrive.ae2.MekanismCreativeFluidTankCellHandler;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(CreativityDrive.MOD_ID)
public class CreativityDrive {
    public static final String MOD_ID = "creativity_drive";

    private static final Logger LOGGER = LogUtils.getLogger();

    public CreativityDrive(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            StorageCells.addCellHandler(MekanismCreativeFluidTankCellHandler.INSTANCE);
            StorageCells.addCellHandler(MekanismCreativeChemicalTankCellHandler.INSTANCE);
            StorageCells.addCellHandler(MekanismCreativeBinCellHandler.INSTANCE);
            LOGGER.info("Registered Mekanism creative tanks and bins as AE2 infinite storage cells");
        });
    }
}
