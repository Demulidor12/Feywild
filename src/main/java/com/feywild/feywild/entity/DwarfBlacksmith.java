package com.feywild.feywild.entity;

import com.feywild.feywild.entity.base.ISummonable;
import com.feywild.feywild.entity.base.ITameable;
import com.feywild.feywild.entity.base.Trader;
import com.feywild.feywild.entity.goals.DwarvenAttackGoal;
import com.feywild.feywild.entity.goals.GoToAnvilPositionGoal;
import com.feywild.feywild.entity.goals.GoToTargetPositionGoal;
import com.feywild.feywild.entity.goals.RefreshStockGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class DwarfBlacksmith extends Trader implements ITameable, ISummonable, IAnimatable {

    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(DwarfBlacksmith.class, EntityDataSerializers.INT);

    private boolean isTamed;

    @Nullable
    private BlockPos summonPos;

    private final AnimationFactory factory = new AnimationFactory(this);

    public DwarfBlacksmith(EntityType<? extends Trader> type, Level level) {
        super(type, level);
        this.noCulling = true;
        this.moveControl = new MoveControl(this);
    }

    public static AttributeSupplier.Builder getDefaultAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, Attributes.MOVEMENT_SPEED.getDefaultValue())
                .add(Attributes.MAX_HEALTH, 36)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.ARMOR_TOUGHNESS, 5)
                .add(Attributes.ARMOR, 15)
                .add(Attributes.ATTACK_DAMAGE, 4)
                .add(Attributes.MOVEMENT_SPEED, 0.35);
    }

    public static boolean canSpawn(EntityType<DwarfBlacksmith> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, Random random) {
        //noinspection deprecation
        if (pos.getY() >= level.getSeaLevel() - 10 || level.canSeeSky(pos) || random.nextDouble() < 0.15) {
            return false;
        } else {
            return checkMobSpawnRules(type, level, reason, pos, random);
        }
    }

    @Override
    public String getTradeCategory() {
        return this.isTamed() ? "tamed" : "untamed";
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(5, new MoveTowardsTargetGoal(this, 0.1f, 8));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.5D));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, GoToTargetPositionGoal.byBlockPos(this, this::getSummonPos, 5, 0.5f));
        this.goalSelector.addGoal(2, new GoToAnvilPositionGoal(this, this::getSummonPos, 5));
        this.goalSelector.addGoal(6, new RefreshStockGoal(this));
        this.targetSelector.addGoal(1, new DwarvenAttackGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STATE, 0);
    }

    @Nonnull
    @Override
    public InteractionResult interactAt(Player player, @Nonnull Vec3 vec, @Nonnull InteractionHand hand) {
        if (!player.getCommandSenderWorld().isClientSide) {
            trade(player);
        }
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }

    protected void trade(Player player) {
        this.setTradingPlayer(player);
        this.openTradingScreen(player, new TranslatableComponent("Dwarven Trader"), 1);
        player.displayClientMessage(new TranslatableComponent("dwarf.feywild.dialogue"), false);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor level, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataNbt) {
        this.restrictTo(this.blockPosition(), 7);
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataNbt);
    }

    @Override
    public boolean isTamed() {
        return this.isTamed;
    }

    @Override
    public boolean trySetTamed(boolean tamed) {
        this.isTamed = tamed;
        return true;
    }

    @Nullable
    @Override
    public BlockPos getSummonPos() {
        return this.summonPos;
    }

    @Override
    public void setSummonPos(@Nullable BlockPos pos) {
        this.summonPos = pos == null ? null : pos.immutable();
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("Tamed", this.isTamed);
        if (this.summonPos != null) {
            nbt.put("SummonPos", NbtUtils.writeBlockPos(this.summonPos));
        } else {
            nbt.remove("SummonPos");
        }
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.isTamed = nbt.getBoolean("Tamed");
        this.summonPos = nbt.contains("SummonPos", Tag.TAG_COMPOUND) ? NbtUtils.readBlockPos(nbt.getCompound("SummonPos")).immutable() : null;
    }

    public State getState() {
        State[] states = State.values();
        return states[Mth.clamp(this.entityData.get(STATE), 0, states.length - 1)];
    }

    public void setState(State state) {
        this.entityData.set(STATE, state.ordinal());
    }

    @Override
    public boolean checkSpawnRules(@Nonnull LevelAccessor levelIn, @Nonnull MobSpawnType spawnReasonIn) {
        return super.checkSpawnRules(levelIn, spawnReasonIn) && this.blockPosition().getY() < 60 && !levelIn.canSeeSky(this.blockPosition());
    }

    @Override
    protected void updateControlFlags() {
        super.updateControlFlags();
        this.goalSelector.setControlFlag(Goal.Flag.MOVE, true);
        this.goalSelector.setControlFlag(Goal.Flag.JUMP, true);
        this.goalSelector.setControlFlag(Goal.Flag.LOOK, true);
    }

    @Override
    protected boolean canRide(@Nonnull Entity entityIn) {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSourceIn) {
        return SoundEvents.VILLAGER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_CELEBRATE;
    }

    @Override
    public float getVoicePitch() {
        return 0.6f;
    }

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        if (!this.dead && !this.isDeadOrDying()) {
            if (this.getState() == State.ATTACKING) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dwarf_blacksmith.smash", false));
                return PlayState.CONTINUE;
            } else if (this.getState() == State.WORKING) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dwarf_blacksmith.craft", true));
                return PlayState.CONTINUE;
            }
        }
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dwarf_blacksmith.walk", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dwarf_blacksmith.stand", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public boolean isPersistenceRequired() {
        return this.isTamed || this.getVillagerXp() > 0 || super.isPersistenceRequired();
    }

    @Override
    public void checkDespawn() {
        if (!this.isPersistenceRequired()) {
            Entity entity = this.level.getNearestPlayer(this, -1.0D);
            net.minecraftforge.eventbus.api.Event.Result result = net.minecraftforge.event.ForgeEventFactory.canEntityDespawn(this);
            if (result == net.minecraftforge.eventbus.api.Event.Result.DENY) {
                noActionTime = 0;
                entity = null;
            } else if (result == net.minecraftforge.eventbus.api.Event.Result.ALLOW) {
                this.remove(RemovalReason.DISCARDED);
                entity = null;
            }
            if (entity != null) {
                double distance = entity.distanceToSqr(this);

                int k = this.getType().getCategory().getNoDespawnDistance();
                int l = k * k;

                if (this.noActionTime > 2400 && this.random.nextInt(800) == 0 && distance > (double) l && this.removeWhenFarAway(distance * 2)) {
                    this.remove(RemovalReason.DISCARDED);
                } else if (distance < (double) l) {
                    this.noActionTime = 0;
                }
            }

        } else {
            this.noActionTime = 0;
        }
    }

    public enum State {
        IDLE, ATTACKING, WORKING
    }
}
