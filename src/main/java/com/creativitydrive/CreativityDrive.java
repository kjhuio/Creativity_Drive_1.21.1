package com.creativitydrive;

import appeng.api.storage.StorageCells;
import com.creativitydrive.ae2.MekanismCreativeBinCellHandler;
import com.creativitydrive.ae2.MekanismCreativeFluidTankCellHandler;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import org.slf4j.Logger;

@Mod(CreativityDrive.MOD_ID)
public class CreativityDrive {
    public static final String MOD_ID = "creativity_drive";

    private static final Logger LOGGER = LogUtils.getLogger();

    public CreativityDrive(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(this::commonSetup);
        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            StorageCells.addCellHandler(MekanismCreativeFluidTankCellHandler.INSTANCE);
            StorageCells.addCellHandler(MekanismCreativeBinCellHandler.INSTANCE);
            if (ModList.get().isLoaded("appflux") && ModList.get().isLoaded("appmek")) {
                AppliedFluxCompat.registerCreativeEnergyCubeCellHandler();
                AppliedFluxCompat.registerCreativeEnergyCubeCellHandler();
                LOGGER.info("Registered Mekanism creative tanks and bins and energy cubes as AE2 infinite storage cells");
            } else if (ModList.get().isLoaded("appmek")&& !ModList.get().isLoaded("appflux")) {
                AppliedMekanisticsCompat.registerChemicalTankCellHandler();
                LOGGER.info("Registered Mekanism creative tanks and bins as AE2 infinite storage cells (energy cube support disabled)");
            } else if (ModList.get().isLoaded("appflux") && !ModList.get().isLoaded("appmek")) {
                AppliedFluxCompat.registerCreativeEnergyCubeCellHandler();
                LOGGER.info("Registered Mekanism creative fluid tanks and bins and energy cubes as AE2 infinite storage cells (chemical tank support disabled)");
            } else {
                LOGGER.info("Registered Mekanism creative fluid tanks and bins as AE2 infinite storage cells (chemical tank and energy cube support disabled)");
            }
        });
    }
}
