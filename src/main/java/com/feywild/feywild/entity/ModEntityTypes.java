package com.feywild.feywild.entity;

import com.feywild.feywild.FeywildMod;
import com.feywild.feywild.entity.boat.FeyBoat;
import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

@RegisterClass
public class ModEntityTypes {

    public static final EntityType<BeeKnight> beeKnight = EntityType.Builder.of(BeeKnight::new, MobCategory.CREATURE)
            .sized(0.7f, 1)
            .build(FeywildMod.getInstance().modid + "_bee_knight");

    public static final EntityType<Mandragora> mandragora = EntityType.Builder.of(Mandragora::new, MobCategory.CREATURE)
            .sized(0.7f, 1)
            .build(FeywildMod.getInstance().modid + "_mandragora");

    public static final EntityType<DwarfBlacksmith> dwarfBlacksmith = EntityType.Builder.of(DwarfBlacksmith::new, MobCategory.MONSTER)
            .sized(1, 1)
            .build(FeywildMod.getInstance().modid + "_dwarf_blacksmith");

    public static final EntityType<MarketDwarf> dwarfToolsmith = EntityType.Builder.of(MarketDwarf::new, MobCategory.MONSTER)
            .sized(1, 1)
            .build(FeywildMod.getInstance().modid + "_dwarf_toolsmith");

    public static final EntityType<MarketDwarf> dwarfArtificer = EntityType.Builder.of(MarketDwarf::new, MobCategory.CREATURE)
            .sized(1, 1)
            .build(FeywildMod.getInstance().modid + "_dwarf_artificer");

    public static final EntityType<MarketDwarf> dwarfBaker = EntityType.Builder.of(MarketDwarf::new, MobCategory.CREATURE)
            .sized(1, 1)
            .build(FeywildMod.getInstance().modid + "_dwarf_baker");

    public static final EntityType<MarketDwarf> dwarfShepherd = EntityType.Builder.of(MarketDwarf::new, MobCategory.CREATURE)
            .sized(1, 1)
            .build(FeywildMod.getInstance().modid + "_dwarf_shepherd");

    public static final EntityType<MarketDwarf> dwarfDragonHunter = EntityType.Builder.of(MarketDwarf::new, MobCategory.CREATURE)
            .sized(1, 1)
            .build(FeywildMod.getInstance().modid + "_dwarf_dragon_hunter");

    public static final EntityType<MarketDwarf> dwarfMiner = EntityType.Builder.of(MarketDwarf::new, MobCategory.CREATURE)
            .sized(1, 1)
            .build(FeywildMod.getInstance().modid + "_dwarf_miner");

    public static final EntityType<SpringPixie> springPixie = EntityType.Builder.of(SpringPixie::new, MobCategory.CREATURE)
            .sized(0.7f, 1)
            .build(FeywildMod.getInstance().modid + "_spring_pixie");

    public static final EntityType<SummerPixie> summerPixie = EntityType.Builder.of(SummerPixie::new, MobCategory.CREATURE)
            .sized(0.7f, 1)
            .build(FeywildMod.getInstance().modid + "_summer_pixie");

    public static final EntityType<AutumnPixie> autumnPixie = EntityType.Builder.of(AutumnPixie::new, MobCategory.CREATURE)
            .sized(0.7f, 1)
            .build(FeywildMod.getInstance().modid + "_autumn_pixie");

    public static final EntityType<WinterPixie> winterPixie = EntityType.Builder.of(WinterPixie::new, MobCategory.CREATURE)
            .sized(0.7f, 1)
            .build(FeywildMod.getInstance().modid + "_winter_pixie");

    /*BOATS*/

    public static final EntityType<FeyBoat> autumnBoat = EntityType.Builder.<FeyBoat>of(FeyBoat::new, MobCategory.MISC)
            .sized(1, 1)
            .build(FeywildMod.getInstance().modid + "_autumn_boat");
}
