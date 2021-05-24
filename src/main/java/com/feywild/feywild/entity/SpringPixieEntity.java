package com.feywild.feywild.entity;

import com.feywild.feywild.entity.goals.GoToSummoningPositionGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class SpringPixieEntity extends FeyEntity implements IAnimatable {

    //TAMED variable
    public static final DataParameter<Boolean> TAMED = EntityDataManager.defineId(SpringPixieEntity.class, DataSerializers.BOOLEAN);
    public BlockPos summonPos;
    //Geckolib variable
    private AnimationFactory factory = new AnimationFactory(this);
    private boolean setBehaviors;

    public SpringPixieEntity(EntityType<? extends FeyEntity> type, World worldIn) {
        super(type, worldIn);
        //Geckolib check
        this.noCulling = true;
        this.moveControl = new FlyingMovementController(this, 4, true);
        addGoalsAfterConstructor();
    }

    public SpringPixieEntity(World worldIn, boolean isTamed, BlockPos pos) {
        super(ModEntityTypes.SPRING_PIXIE.get(), worldIn);
        //Geckolib check
        this.noCulling = true;
        this.moveControl = new FlyingMovementController(this, 4, true);
        this.entityData.set(TAMED, isTamed);
        this.summonPos = pos;
        addGoalsAfterConstructor();
    }

    /* QUEST */

    public void setTag(SpringPixieEntity entity) {
        entity.addTag("spring_quest_pixie");
    }

    /*
    @Override
    public ActionResultType interactAt(PlayerEntity player, Vector3d vec, Hand hand) {
        if (player.getCommandSenderWorld().isClientSide) return ActionResultType.SUCCESS;

        if (player.getItemInHand(hand).isEmpty()) {
            if(this.getTags().contains("spring_quest_pixie")){

            player.sendMessage(new TranslationTextComponent("spring_quest_pixie.feywild.quest_01_message"), player.getUUID());

                return ActionResultType.SUCCESS;

            }
        }

        return ActionResultType.SUCCESS;
    }  */



    /* Animation */

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {


        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pixie.fly", true));

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {

        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {

        return this.factory;
    }


    /* GOALS */

    @Override
    protected void registerGoals() { }


    protected void addGoalsAfterConstructor() {
        if (this.level.isClientSide())
            return;

        for (PrioritizedGoal goal : getGoals()) {
            this.goalSelector.addGoal(goal.getPriority(), goal.getGoal());
        }
    }

    public List<PrioritizedGoal> getGoals() {
        return this.entityData.get(TAMED) ? getTamedGoals() : getUntamedGoals();
    }

    public List<PrioritizedGoal> getTamedGoals() {
        List<PrioritizedGoal> list = new ArrayList<>();
        list.add(new PrioritizedGoal(0, new SwimGoal(this)));
        list.add(new PrioritizedGoal(2, new LookAtGoal(this, PlayerEntity.class, 8.0f)));
        list.add(new PrioritizedGoal(3, new GoToSummoningPositionGoal(this, () -> this.summonPos, 10)));
        list.add(new PrioritizedGoal(2, new LookRandomlyGoal(this)));
        list.add(new PrioritizedGoal(3, new WaterAvoidingRandomFlyingGoal(this, 1.0D)));

        return list;
    }

    public List<PrioritizedGoal> getUntamedGoals() {
        List<PrioritizedGoal> list = new ArrayList<>();
        list.add(new PrioritizedGoal(0, new SwimGoal(this)));
        list.add(new PrioritizedGoal(2, new LookAtGoal(this, PlayerEntity.class, 8.0f)));
        list.add(new PrioritizedGoal(1, new TemptGoal(this, 1.25D,
                Ingredient.of(Items.COOKIE), false)));
        list.add(new PrioritizedGoal(3, new WaterAvoidingRandomFlyingGoal(this, 1.0D)));
        list.add(new PrioritizedGoal(2, new LookRandomlyGoal(this)));


        return list;

    }

    /* SAVE DATA */

    //write
    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        if (summonPos != null) {
            tag.putInt("summonPos_X", summonPos.getX());
            tag.putInt("summonPos_Y", summonPos.getY());
            tag.putInt("summonPos_Z", summonPos.getZ());
        }

        this.entityData.set(TAMED, tag.getBoolean("tamed"));
    }


    //read
    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("summoner_x"))
            summonPos = new BlockPos(tag.getInt("summonPos_X"), tag.getInt("summonPos_Y"), tag.getInt("summonPos_Z"));

        // this.entityData.set(TAMED, tag.getBoolean("tamed"));

        if (tag.contains("tamed")) {
            this.setTamed(tag.getBoolean("tamed"));
        }

        if (!setBehaviors) {
            tryResetGoals();
            setBehaviors = true;
        }
    }

    public void tryResetGoals() {
        this.goalSelector.availableGoals = new LinkedHashSet<>();
        this.addGoalsAfterConstructor();
    }


    public void setTamed(boolean isTamed) {
        this.entityData.set(TAMED, isTamed);

    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        this.entityData.define(TAMED, false);

    }


    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return false;
    }
}
