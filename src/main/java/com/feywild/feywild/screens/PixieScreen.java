package com.feywild.feywild.screens;

import com.feywild.feywild.FeywildMod;
import com.feywild.feywild.container.PixieContainer;
import com.feywild.feywild.network.FeywildPacketHandler;
import com.feywild.feywild.network.QuestMessage;
import com.feywild.feywild.quest.QuestMap;
import com.feywild.feywild.util.ModUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class PixieScreen extends ContainerScreen<PixieContainer> {

    private final ResourceLocation GUI = new ResourceLocation(FeywildMod.MOD_ID,
            "textures/gui/pixie_quest_gui.png");

    private PixieContainer container;
    public PixieScreen(PixieContainer container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
        this.container = container;
    }

   @Override
    protected void init() {
        super.init();
        //Ancient's note : test button will be repurposed for quest completion
       if(QuestMap.getCanSkip(container.getQuest()))
        addButton(new Button(this.width/2 - width / 3 + 20, this.height/2 + this.getYSize()/3,20,20, new StringTextComponent("X"),button -> {
            this.acceptQuest();
        }));
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

  public void acceptQuest(){
         FeywildPacketHandler.INSTANCE.sendToServer(new QuestMessage(null, container.getQuest()));

        this.onClose();
    }


    @Override //drawGuiContainerForegroundLayer
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {

        // Add system for different quest branches
        for(int i =1; i <= container.getLines(); i++){
            drawString(matrixStack, Minecraft.getInstance().font, new TranslationTextComponent("spring_quest_pixie.feywild.quest_"+Math.abs(container.getQuest())+"_message_" + i), -width /6, 9 * i, 0xffffff);
        }
    }

    @Override //drawGuiContainerBackgroundLayer
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {

        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(GUI);
        int i = this.leftPos - (this.imageWidth/4);
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, 256, this.imageHeight); //might be wrong
    }
}
