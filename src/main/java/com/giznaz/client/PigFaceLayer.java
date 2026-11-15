package com.giznaz.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "giznaz", bus = Mod.EventBusSubscriber.Bus.MOD, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public final class PigFaceLayer {
    private static final ResourceLocation FACE =
            ResourceLocation.fromNamespaceAndPath("giznaz", "textures/entity/giznaz_face.png");

    private PigFaceLayer() {}

    @SubscribeEvent
    public static void addPigFace(EntityRenderersEvent.AddLayers event) {
        PigRenderer renderer = event.getEntityRenderer(EntityType.PIG);
        if (renderer != null) {
            renderer.addLayer(new FaceLayer(renderer, event.getEntityModels().bakeLayer(ModelLayers.PIG)));
        }
    }

    private static final class FaceLayer extends RenderLayer<Pig, PigModel<Pig>> {
        private final ExposedPigModel faceModel;

        private FaceLayer(RenderLayerParent<Pig, PigModel<Pig>> parent, ModelPart root) {
            super(parent);
            this.faceModel = new ExposedPigModel(root);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Pig pig,
                           float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                           float netHeadYaw, float headPitch) {
            if (pig.isInvisible()) return;

            faceModel.young = pig.isBaby();
            faceModel.setupAnim(pig, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            poseStack.pushPose();
            if (pig.isBaby()) {
                poseStack.scale(0.5F, 0.5F, 0.5F);
                poseStack.translate(0.0F, 1.5F, 0.0F);
            }
            faceModel.faceHead().translateAndRotate(poseStack);

            VertexConsumer vertices = buffer.getBuffer(RenderType.entityCutoutNoCull(FACE));
            PoseStack.Pose pose = poseStack.last();
            float z = -9.08F;
            vertex(vertices, pose, -4, -4, z, 0, 0, packedLight);
            vertex(vertices, pose, -4,  4, z, 0, 1, packedLight);
            vertex(vertices, pose,  4,  4, z, 1, 1, packedLight);
            vertex(vertices, pose,  4, -4, z, 1, 0, packedLight);

            poseStack.popPose();
        }

        private static void vertex(VertexConsumer out, PoseStack.Pose pose, float x, float y, float z,
                                   float u, float v, int light) {
            out.addVertex(pose, x, y, z)
                    .setColor(255, 255, 255, 255)
                    .setUv(u, v)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(pose, 0.0F, 0.0F, -1.0F);
        }
    }

    private static final class ExposedPigModel extends PigModel<Pig> {
        private ExposedPigModel(ModelPart root) {
            super(root);
        }

        private ModelPart faceHead() {
            return this.head;
        }
    }
}
