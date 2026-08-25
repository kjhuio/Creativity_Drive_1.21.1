package com.creativitydrive;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;


@EventBusSubscriber(modid = CreativityDrive.MOD_ID)
public final class ModCommands {

    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("creativitydrive")
                .requires(source -> source.hasPermission(4))
                .then(replicationSubcommand())
        );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> replicationSubcommand() {
        return net.minecraft.commands.Commands.literal("replication")
                .executes(ModCommands::showReplication)
                .then(Commands.literal("on").executes(ctx -> setReplication(ctx,true)))
                .then(Commands.literal("off").executes(ctx -> setReplication(ctx,false)));
    }

    private static int showReplication(CommandContext<CommandSourceStack> ctx) {

        boolean current = Config.ALLOW_SELF_REPLICATION.get();

        ctx.getSource().sendSuccess(() -> Component.literal("[Creativity Drive] allowSelfReplication = " + current),false);

        return 1;
    }

    private static int setReplication(CommandContext<CommandSourceStack> ctx, boolean value) {

        Config.ALLOW_SELF_REPLICATION.set(value);
        String executor = ctx.getSource().getTextName();

        ctx.getSource().sendSuccess(() -> Component.literal("[Creativity Drive] allowSelfReplication = " + value),false);
        LOGGER.info("allowSelfReplication changed to {} by {}", value, executor);

        return 1;
    }

}
