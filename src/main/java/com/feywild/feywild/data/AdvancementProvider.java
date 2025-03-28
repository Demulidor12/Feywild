package com.feywild.feywild.data;

import com.feywild.feywild.block.ModBlocks;
import com.feywild.feywild.block.ModTrees;
import com.feywild.feywild.entity.ModEntityTypes;
import com.feywild.feywild.item.ModItems;
import com.feywild.feywild.world.dimension.ModDimensions;
import io.github.noeppi_noeppi.libx.annotation.data.Datagen;
import io.github.noeppi_noeppi.libx.data.provider.AdvancementProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.TradeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

@Datagen
public class AdvancementProvider extends AdvancementProviderBase {

    public AdvancementProvider(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @Override
    public void setup() {
        this.root()
                .display(ModItems.feywildLexicon)
                .background(new ResourceLocation("minecraft", "textures/gui/advancements/backgrounds/adventure.png"))
                .task(this.items(ModItems.feywildLexicon));

        this.advancement("fey_dust")
                .display(ModItems.feyDust)
                .task(this.items(ModItems.feyDust), this.items(ModItems.lesserFeyGem));

        this.advancement("dwarf_trade").parent("fey_dust")
                .display(ModItems.lesserFeyGem)
                .task(new TradeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, this.entity(ModEntityTypes.dwarfBlacksmith), this.stack(ModItems.lesserFeyGem)));

        this.advancement("dwarf_contract").parent("dwarf_trade")
                .display(ModItems.summoningScrollDwarfBlacksmith)
                .task(this.items(ModItems.summoningScrollDwarfBlacksmith));

        this.advancement("schematics_gem_transmutation").parent("dwarf_contract")
                .display(ModItems.schematicsGemTransmutation)
                .task(this.items(ModItems.schematicsGemTransmutation));

        this.advancement("fey_altar")
                .display(ModBlocks.feyAltar)
                .task(this.items(ModBlocks.feyAltar));

        this.advancement("fey_sapling")
                .display(ModTrees.springTree.getSapling())
                .task(
                        this.items(ModTrees.springTree.getSapling()),
                        this.items(ModTrees.summerTree.getSapling()),
                        this.items(ModTrees.autumnTree.getSapling()),
                        this.items(ModTrees.winterTree.getSapling())
                );

        this.advancement("summon_spring").parent("fey_altar")
                .display(ModItems.summoningScrollSpringPixie)
                .task(this.items(ModItems.summoningScrollSpringPixie));

        this.advancement("summon_summer").parent("fey_altar")
                .display(ModItems.summoningScrollSummerPixie)
                .task(this.items(ModItems.summoningScrollSummerPixie));

        this.advancement("summon_autumn").parent("fey_altar")
                .display(ModItems.summoningScrollAutumnPixie)
                .task(this.items(ModItems.summoningScrollAutumnPixie));

        this.advancement("summon_winter").parent("fey_altar")
                .display(ModItems.summoningScrollWinterPixie)
                .task(this.items(ModItems.summoningScrollWinterPixie));

        this.advancement("traveling_stone")
                .display(ModItems.inactiveMarketRuneStone)
                .task(this.items(ModItems.inactiveMarketRuneStone), this.items(ModItems.marketRuneStone));

        this.advancement("dwarven_market").parent("traveling_stone")
                .display(ModItems.marketRuneStone)
                .task(new ChangeDimensionTrigger.TriggerInstance(this.entity(EntityType.PLAYER), Level.OVERWORLD, ModDimensions.MARKET_PLACE_DIMENSION));

        this.advancement("dwarf_trades").parent("dwarven_market")
                .display(ModItems.marketRuneStone)
                .task(
                        new TradeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, this.entity(ModEntityTypes.dwarfBaker), ItemPredicate.ANY),
                        new TradeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, this.entity(ModEntityTypes.dwarfShepherd), ItemPredicate.ANY),
                        new TradeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, this.entity(ModEntityTypes.dwarfArtificer), ItemPredicate.ANY),
                        new TradeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, this.entity(ModEntityTypes.dwarfToolsmith), ItemPredicate.ANY),
                        new TradeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, this.entity(ModEntityTypes.dwarfMiner), ItemPredicate.ANY),
                        new TradeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, this.entity(ModEntityTypes.dwarfDragonHunter), ItemPredicate.ANY)
                );

        this.advancement("honeycomb")
                .display(ModItems.honeycomb)
                .task(this.items(ModItems.honeycomb));

        this.advancement("magical_honey_cookie").parent("honeycomb")
                .display(ModItems.magicalHoneyCookie)
                .task(this.items(ModItems.magicalHoneyCookie));

    }
}
