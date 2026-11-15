package com.giznaz;

import com.giznaz.registry.GiznazEntities;
import com.giznaz.registry.GiznazItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GiznazMod.MOD_ID)
public final class GiznazMod {
    public static final String MOD_ID = "giznaz";

    public GiznazMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        GiznazItems.register(modBus);
        GiznazEntities.register(modBus);
    }
}
