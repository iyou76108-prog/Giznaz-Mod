package com.giznaz.registry;

import com.giznaz.GiznazMod;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class GiznazItems {
    private GiznazItems() {}

    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, GiznazMod.MOD_ID);

    public static final RegistryObject<Item> POOP = ITEMS.register("poop",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> FERTILIZER = ITEMS.register("giznaz_fertilizer",
            () -> new BoneMealItem(new Item.Properties()));

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
