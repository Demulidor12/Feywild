package com.feywild.feywild.data;

import com.feywild.feywild.block.DisplayGlassBlock;
import com.feywild.feywild.block.ModBlocks;
import com.feywild.feywild.block.flower.CrocusBlock;
import com.feywild.feywild.block.flower.DandelionBlock;
import com.feywild.feywild.block.flower.GiantFlowerBlock;
import com.feywild.feywild.block.flower.SunflowerBlock;
import com.feywild.feywild.block.trees.BaseSaplingBlock;
import com.feywild.feywild.block.trees.FeyLeavesBlock;
import com.feywild.feywild.block.trees.ILogBlock;
import io.github.noeppi_noeppi.libx.annotation.data.Datagen;
import io.github.noeppi_noeppi.libx.data.provider.BlockStateProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

@Datagen
public class BlockStateProvider extends BlockStateProviderBase {

    public BlockStateProvider(ModX mod, DataGenerator generator, ExistingFileHelper fileHelper) {
        super(mod, generator, fileHelper);
    }

    @Override
    protected void setup() {
        this.manualModel(ModBlocks.dwarvenAnvil);
        this.manualModel(ModBlocks.feyAltar);
        this.manualModel(ModBlocks.libraryBell);
        this.manualModel(ModBlocks.treeMushroom);
        this.manualModel(ModBlocks.magicalBrazier);
        this.manualModel(ModBlocks.feyMushroom);

        //noinspection ConstantConditions
        this.manualModel(ModBlocks.ancientRunestone, this.models().cubeTop(
                ModBlocks.ancientRunestone.getRegistryName().getPath(),
                this.modLoc("block/" + ModBlocks.ancientRunestone.getRegistryName().getPath() + "_side"),
                this.modLoc("block/" + ModBlocks.ancientRunestone.getRegistryName().getPath() + "_top")
        ));
    }

    @Override
    protected void defaultState(ResourceLocation id, Block block, Supplier<ModelFile> model) {
        if (block instanceof FeyLeavesBlock) {
            this.simpleBlock(block,
                    new ConfiguredModel(this.cubeAll(block)),
                    new ConfiguredModel(this.models().cubeAll(id.getPath() + "_02", new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_02")))
            );
        } else if (block instanceof DisplayGlassBlock) {
            VariantBlockStateBuilder builder = this.getVariantBuilder(block);
            DisplayGlassBlock.BREAKAGE.getPossibleValues().forEach(i ->
                    builder.partialState().with(DisplayGlassBlock.BREAKAGE, i)
                            .addModels(
                                    new ConfiguredModel(this.models().cubeAll(
                                            id.getPath() + (i == 0 ? "" : i),
                                            new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + i)
                                    ))
                            )
            );
        } else if (block instanceof RotatedPillarBlock rotatedBlock && block instanceof ILogBlock logBlock) {
            this.logAxis(rotatedBlock, id, logBlock.getWoodBlock());
        } else if (block instanceof CropBlock) {
            VariantBlockStateBuilder builder = this.getVariantBuilder(block);
            //noinspection CodeBlock2Expr
            BlockStateProperties.AGE_7.getPossibleValues().forEach(i -> {
                builder.partialState().with(BlockStateProperties.AGE_7, i).addModels(
                        new ConfiguredModel(this.models().withExistingParent(id.getPath() + i, new ResourceLocation("minecraft", "block/crop"))
                                .texture("crop", new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + i))
                        )
                );
            });
        } else if (block instanceof GiantFlowerBlock) {
            VariantBlockStateBuilder builder = this.getVariantBuilder(block);
            // 0 and 2 only for particles
            builder.partialState().with(GiantFlowerBlock.PART, 0).addModels(new ConfiguredModel(this.models().getExistingFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_stem"))));
            builder.partialState().with(GiantFlowerBlock.PART, 2).addModels(new ConfiguredModel(this.models().getExistingFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_stem"))));
            builder.partialState().with(GiantFlowerBlock.PART, 1).addModels(
                    new ConfiguredModel(this.models().getExistingFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_stem"))),
                    new ConfiguredModel(this.models().getExistingFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_stem")), 0, 90, false),
                    new ConfiguredModel(this.models().getExistingFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_stem")), 0, 180, false),
                    new ConfiguredModel(this.models().getExistingFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_stem")), 0, 270, false)
            );
            if (block == ModBlocks.sunflower) {
                //noinspection CodeBlock2Expr
                SunflowerBlock.TIME_VARIANT.getPossibleValues().forEach(i -> {
                    builder.partialState().with(GiantFlowerBlock.PART, 3).with(SunflowerBlock.TIME_VARIANT, i).addModels(
                            new ConfiguredModel(this.models().getExistingFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_flower" + i)))
                    );
                });
            } else if (block == ModBlocks.dandelion) {
                builder.partialState().with(GiantFlowerBlock.PART, 3).with(DandelionBlock.VARIANT, 0).addModels(
                        new ConfiguredModel(this.models().getExistingFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_bud")))
                );
                builder.partialState().with(GiantFlowerBlock.PART, 3).with(DandelionBlock.VARIANT, 1).addModels(
                        new ConfiguredModel(this.models().getExistingFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_flower")))
                );
                builder.partialState().with(GiantFlowerBlock.PART, 3).with(DandelionBlock.VARIANT, 2).addModels(
                        new ConfiguredModel(this.models().getExistingFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_fluff")))
                );
                builder.partialState().with(GiantFlowerBlock.PART, 3).with(DandelionBlock.VARIANT, 3).addModels(
                        new ConfiguredModel(this.models().getExistingFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_nofluff")))
                );
            } else if (block == ModBlocks.crocus) {
                //noinspection CodeBlock2Expr
                CrocusBlock.OPENING_STATE.getPossibleValues().forEach(i -> {
                    builder.partialState().with(GiantFlowerBlock.PART, 3).with(CrocusBlock.OPENING_STATE, i).addModels(
                            new ConfiguredModel(this.models().getExistingFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + (i == 0 ? "_bud" : "_flower" + i))))
                    );
                });
            } else {
                builder.partialState().with(GiantFlowerBlock.PART, 3).addModels(
                        new ConfiguredModel(this.models().getExistingFile(new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_flower")))
                );
            }
        } else {
            super.defaultState(id, block, model);
        }
    }

    @Override
    protected ModelFile defaultModel(ResourceLocation id, Block block) {
        if (block instanceof BaseSaplingBlock) {
            return this.models().cross(id.getPath(), this.blockTexture(block));
        } else {
            return super.defaultModel(id, block);
        }
    }

    private <T extends RotatedPillarBlock & ILogBlock> void logAxis(RotatedPillarBlock logBlock, ResourceLocation id, Block woodBlock) {
        this.axisBlock(logBlock, this.blockTexture(woodBlock), new ResourceLocation(id.getNamespace(), "block/" + id.getPath() + "_top"));

    }
}
