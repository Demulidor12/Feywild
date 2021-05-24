package com.feywild.feywild.util;

import com.electronwill.nightconfig.core.AbstractConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.ConfigFormat;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MobConfig extends AbstractConfig {

    protected String name;
    protected int weight;
    protected int min;
    protected int max;

    protected List<String> biomes;
    protected BiomeDictionary.Type restriction;

    //UNKNOWN
    protected ForgeConfigSpec.IntValue configWeight;
    protected ForgeConfigSpec.IntValue configMin;
    protected ForgeConfigSpec.IntValue configMax;
    protected ForgeConfigSpec.ConfigValue<String> configBiomes;

    private int cachedWeight = -9999;
    private int cachedMin = -9999;
    private int cachedMax = -9999;
    private List<BiomeDictionary.Type> cachedBiomes = null;

    //CONSTRUCTOR boolean concurrent has to be added...
    public MobConfig(String name, int weight, int min, int max, List<String> biomes) {
        this(name, weight, min, max, biomes, BiomeDictionary.Type.OVERWORLD, true);
    }

    public MobConfig(String name, int weight, int min, int max, List<String> biomes, BiomeDictionary.Type restriction, boolean concurrent) {
        super(concurrent);
        this.name = name;
        this.weight = weight;
        this.min = min;
        this.max = max;
        this.biomes = biomes;
        this.restriction = restriction;
    }

    public BiomeDictionary.Type getRestriction() {
        return restriction;
    }

    public int getWeight() {
        if (cachedWeight == -9999) {
            cachedWeight = configWeight.get();
        }
        return cachedWeight;
    }

    public int getMin() {
        if (cachedMin == -9999) {
            cachedMin = configMin.get();
        }
        return cachedMin;
    }

    public int getMax() {
        if (cachedMax == -9999) {
            cachedMax = configMax.get();
        }
        return cachedMax;
    }

    public List<BiomeDictionary.Type> getBiomes() {
        if (cachedBiomes == null) {
            cachedBiomes = Stream.of(configBiomes.get().split(",")).map(o -> BiomeDictionary.Type.getType(o)).collect(Collectors.toList());
        }
        return cachedBiomes;
    }

    public boolean shouldRegister() {
        return getWeight() > 0;
    }

    protected void preApply(ForgeConfigSpec.Builder builder) {
    }

    protected void doApply(ForgeConfigSpec.Builder builder) {
        builder.comment(name + " spawn config.").push(name + "_spawn");
        configWeight = builder.comment("Chance to spawn (set to 0 to disable).").defineInRange("spawnChance", weight, 0, 256);
        configMin = builder.comment("Min to spawn in a group.").defineInRange("min", min, 0, 256);
        configMax = builder.comment("Max to spawn in a group.").defineInRange("max", max, 0, 256);

        StringBuilder sb = new StringBuilder();
        biomes.forEach(biome -> {
            sb.append(biome);
            sb.append(",");
        });

        configBiomes = builder.comment("List of biome types to spawn.").define("biomes", sb.toString());
    }

    protected void postApply(ForgeConfigSpec.Builder builder) {
        builder.pop();
    }

    //@Override
    public void apply(ForgeConfigSpec.Builder builder) {
        preApply(builder);
        doApply(builder);
        postApply(builder);
    }


    @Override
    public AbstractConfig clone() {
        return null;
    }

    @Override
    public Config createSubConfig() {
        return null;
    }

    @Override
    public ConfigFormat<?> configFormat() {
        return null;
    }
}
