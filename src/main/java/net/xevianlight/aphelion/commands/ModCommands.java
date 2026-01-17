package net.xevianlight.aphelion.commands;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.xevianlight.aphelion.Aphelion;

@EventBusSubscriber(modid = Aphelion.MOD_ID)
public class ModCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        AphelionCommand.register(event.getDispatcher());
    }
}
