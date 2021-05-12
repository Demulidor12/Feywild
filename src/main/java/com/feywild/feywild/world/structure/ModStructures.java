package com.feywild.feywild.world.structure;

import com.feywild.feywild.FeywildMod;
import com.feywild.feywild.world.structure.structures.BlacksmithStructure;
import com.feywild.feywild.world.structure.structures.SpringWorldTreeStructure;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;


public class ModStructures {

            public static final DeferredRegister<Structure<?>> STRUCTURES
                    = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, FeywildMod.MOD_ID);

            public static final RegistryObject<Structure<NoFeatureConfig>> SPRING_WORLD_TREE = STRUCTURES.register("spring_world_tree",
                   SpringWorldTreeStructure::new);

            public static final RegistryObject<Structure<NoFeatureConfig>> BLACKSMITH = STRUCTURES.register("blacksmith",
                    BlacksmithStructure::new);
    /*
                public static void register() {}


                private static <T extends Structure<NoFeatureConfig>>RegistryObject<T> register(String name, Supplier<T> structure) {

                    return Registration.STRUCTURES.register(name, structure);
                }
    */

    public static void setupStructures() {
        setupMapSpacingAndLand(SPRING_WORLD_TREE.get(),
                new StructureSeparationSettings(SpringWorldTreeStructure.AVERAGE_DISTANCE_BETWEEN_CHUNKS,
                        SpringWorldTreeStructure.MIN_DISTANCE_BETWEEN_CHUNKS,
                        SpringWorldTreeStructure.SEED_MODIFIER),
                true);

        setupMapSpacingAndLand(BLACKSMITH.get(),
                new StructureSeparationSettings(BlacksmithStructure.AVERAGE_DISTANCE_BETWEEN_CHUNKS,
                        BlacksmithStructure.MIN_DISTANCE_BETWEEN_CHUNKS,
                        BlacksmithStructure.SEED_MODIFIER),
                true);

        // Add more structures here

}


    //Checks rarity of structure and determines if land conforms to it
    public static <F extends Structure<?>> void setupMapSpacingAndLand(F structure, StructureSeparationSettings structureSeparationSettings,
            boolean transformSurroundingLand) {

        //add our structures into the map in Structure class
        //STRUCTURES_REGISTRY
        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);  //Might return Null

        //Whether surrounding land will be modified automatically to conform to the bottom of the structure.
        //NOISE_AFFECTING_FEATURES
        if(transformSurroundingLand){
            Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder()
                    .addAll(Structure.NOISE_AFFECTING_FEATURES)
                    .add(structure)
                    .build();

        }

        //DEFAULT
        DimensionStructuresSettings.DEFAULTS =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.DEFAULTS)
                        .put(structure, structureSeparationSettings)
                        .build();

        //NOISE_GENERATOR_SETTINGS
        WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().structureSettings().structureConfig();

            if(structureMap instanceof ImmutableMap){
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureSeparationSettings);
                settings.getValue().structureSettings().structureConfig();


            } else {
                structureMap.put(structure, structureSeparationSettings);
            }

        });
    }

}
