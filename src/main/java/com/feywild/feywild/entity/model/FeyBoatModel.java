package com.feywild.feywild.entity.model;

import com.feywild.feywild.FeywildMod;
import com.feywild.feywild.entity.boat.FeyBoat;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class FeyBoatModel<T extends FeyBoat> extends EntityModel<T> {

    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(FeywildMod.getInstance().modid, "boat/autumn_boat"), "main");
    private final ModelPart front;
    private final ModelPart paddle_left;
    private final ModelPart paddle_right;
    private final ModelPart bottom_no_water;
    private final ModelPart left;
    private final ModelPart right;
    private final ModelPart back;
    private final ModelPart bottom;
    private final ImmutableList<ModelPart> parts;

    public FeyBoatModel(ModelPart root) {
        this.front = root.getChild("front");
        this.paddle_left = root.getChild("paddle_left");
        this.paddle_right = root.getChild("paddle_right");
        this.bottom_no_water = root.getChild("bottom_no_water");
        this.left = root.getChild("left");
        this.right = root.getChild("right");
        this.back = root.getChild("back");
        this.bottom = root.getChild("bottom");
        this.parts = ImmutableList.of(bottom.getChild("bottom"), back.getChild("back"), front.getChild("front"),
                paddle_right.getChild("right"), paddle_left.getChild("left"), this.paddle_left, this.paddle_right);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition front = partdefinition.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, 19).addBox(-9.0F, -7.0F, 18.0F, 18.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 22.0F, 5.5F, 0.0F, 3.1416F, 0.0F));

        PartDefinition front2 = front.addOrReplaceChild("front2", CubeListBuilder.create().texOffs(0, 27).addBox(-8.0F, -3.0F, -0.5F, 16.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, -10.5F, 0.0F, 3.1416F, 0.0F));

        PartDefinition sides = front.addOrReplaceChild("sides", CubeListBuilder.create().texOffs(0, 35).addBox(0.5F, -31.0F, -7.0F, 28.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, 24.0F, -10.5F, 0.0F, -1.5708F, 0.0F));

        PartDefinition sides2 = sides.addOrReplaceChild("sides2", CubeListBuilder.create().texOffs(0, 43).addBox(-13.5F, -3.0F, -1.0F, 28.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, -28.0F, -24.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition base = front.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-13.5F, -8.0F, -2.0F, 28.0F, 16.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.5F, 1.5708F, 1.5708F, 0.0F));

        PartDefinition paddle_left = partdefinition.addOrReplaceChild("paddle_left", CubeListBuilder.create().texOffs(62, 0).addBox(-28.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(62, 0).addBox(-28.01F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(13.5F, 18.0F, -4.0F));

        PartDefinition paddle_right = partdefinition.addOrReplaceChild("paddle_right", CubeListBuilder.create().texOffs(62, 20).addBox(26.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(62, 20).addBox(27.01F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-13.5F, 18.0F, -4.0F));

        PartDefinition bottom_no_water = partdefinition.addOrReplaceChild("bottom_no_water", CubeListBuilder.create().texOffs(60, 40).addBox(-14.0F, -9.0F, -6.0F, 28.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 35.0F, 3.5F));

        PartDefinition left = partdefinition.addOrReplaceChild("left", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition right = partdefinition.addOrReplaceChild("right", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition back = partdefinition.addOrReplaceChild("back", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bottom = partdefinition.addOrReplaceChild("bottom", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    private static void animatePaddle(FeyBoat p_170465_, int p_170466_, ModelPart p_170467_, float p_170468_) {
        float f = p_170465_.getRowingTime(p_170466_, p_170468_);
        p_170467_.xRot = Mth.clampedLerp((-(float) Math.PI / 3F), -0.2617994F, (Mth.sin(-f) + 1.0F) / 2.0F);
        p_170467_.yRot = Mth.clampedLerp((-(float) Math.PI / 4F), ((float) Math.PI / 4F), (Mth.sin(-f + 1.0F) + 1.0F) / 2.0F);
        if (p_170466_ == 1) {
            p_170467_.yRot = (float) Math.PI - p_170467_.yRot;
        }
    }

    public void setupAnim(FeyBoat p_102269_, float p_102270_, float p_102271_, float p_102272_, float p_102273_, float p_102274_) {
        animatePaddle(p_102269_, 0, this.paddle_left, p_102270_);
        animatePaddle(p_102269_, 1, this.paddle_right, p_102270_);
    }

    public ImmutableList<ModelPart> parts() {
        return this.parts;
    }

    public ModelPart waterPatch() {
        return this.bottom_no_water;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        front.render(poseStack, buffer, packedLight, packedOverlay);
        paddle_left.render(poseStack, buffer, packedLight, packedOverlay);
        paddle_right.render(poseStack, buffer, packedLight, packedOverlay);
        bottom_no_water.render(poseStack, buffer, packedLight, packedOverlay);
        left.render(poseStack, buffer, packedLight, packedOverlay);
        right.render(poseStack, buffer, packedLight, packedOverlay);
        back.render(poseStack, buffer, packedLight, packedOverlay);
        bottom.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
