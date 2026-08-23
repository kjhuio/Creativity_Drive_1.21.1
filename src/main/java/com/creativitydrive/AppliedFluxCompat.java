package com.creativitydrive;

import appeng.api.storage.StorageCells;
import com.creativitydrive.compat.appflux.MekanismCreativeEnergyCubeCellHandler;

final class AppliedFluxCompat {
    static void registerCreativeEnergyCubeCellHandler() {
        StorageCells.addCellHandler(MekanismCreativeEnergyCubeCellHandler.INSTANCE);
    }
}
