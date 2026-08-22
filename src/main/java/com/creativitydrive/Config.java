package com.creativitydrive;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ALLOW_SELF_REPLICATION = BUILDER.comment(
            "If true, an EMPTY Mekanism creative fluid tank / chemical tank / bin placed in an AE2",
            "ME Drive will act as an infinite source of itself (i.e. it can be duplicated).",
            "If false, empty creative tanks/bins are ignored by AE2 and only provide infinite",
            "storage once a fluid/chemical/item has actually been set in them.",
            "Set to false to prevent players from duplicating creative tanks/bins."
            ).define("allowSelfReplication", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
