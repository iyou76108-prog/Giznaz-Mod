package com.giznaz.client;

import com.giznaz.GiznazMod;
import com.giznaz.entity.GiznazEntity;
import com.giznaz.entity.PoopProjectile;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GiznazMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class GiznazRenderer {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/zombie/zombie.png");

    private GiznazRenderer() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(com.giznaz.registry.GiznazEntities.GIZNAZ.get(), GiznazMobRenderer::new);
        event.registerEntityRenderer(com.giznaz.registry.GiznazEntities.POOP_PROJECTILE.get(),
                context -> new ThrownItemRenderer<>(context, 0.8F, true));
    }

    private static final class GiznazMobRenderer extends MobRenderer<GiznazEntity, ZombieModel<GiznazEntity>> {
        private GiznazMobRenderer(EntityRendererProvider.Context context) {
            super(context, new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.5F);
        }

        @Override
        public ResourceLocation getTextureLocation(GiznazEntity entity) {
            return TEXTURE;
        }

        @Override
        public void render(GiznazEntity entity, float entityYaw, float partialTick,
                           net.minecraft.client.renderer.MultiBufferSource buffer, int packedLight) {
            this.model.crouching = entity.isShiftKeyDown();
            super.render(entity, entityYaw, partialTick, buffer, packedLight);
        }
    }
}
