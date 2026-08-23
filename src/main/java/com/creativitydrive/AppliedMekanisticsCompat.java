package com.creativitydrive;

import appeng.api.storage.StorageCells;
import com.creativitydrive.ae2.MekanismCreativeChemicalTankCellHandler;

/**
 * Isolated in its own class so that MekanismCreativeChemicalTankCellHandler
 * (and by extension appmek's MekanismKey class) is only loaded by the JVM
 * when this class is actually referenced, i.e. only when appmek is present.
 */
final class AppliedMekanisticsCompat {
    static void registerChemicalTankCellHandler() {
        StorageCells.addCellHandler(MekanismCreativeChemicalTankCellHandler.INSTANCE);
    }
}