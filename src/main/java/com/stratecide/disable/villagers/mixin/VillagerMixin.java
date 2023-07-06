package com.stratecide.disable.villagers.mixin;

import com.stratecide.disable.villagers.DisableVillagersMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public abstract class VillagerMixin extends MerchantEntity {
    @Shadow private int experience;

    public VillagerMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tickInject(CallbackInfo ci) {
        if (DisableVillagersMod.killVillagers && (!DisableVillagersMod.spareExperiencedVillagers || experience == 0) && !this.isDead()) {
            this.kill();
            ci.cancel();
        }
    }

    @Inject(method = "isReadyToBreed", at = @At("HEAD"), cancellable = true)
    private void isReadyToBreedInject(CallbackInfoReturnable<Boolean> cir) {
        if (!DisableVillagersMod.breeding) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "onInteractionWith", at=@At("HEAD"), cancellable = true)
    private void finishConversionInject(EntityInteraction interaction, Entity player, CallbackInfo ci) {
        if (DisableVillagersMod.curedZombieLoot != null && interaction == EntityInteraction.ZOMBIE_VILLAGER_CURED) {
            DamageSource source = getWorld().getDamageSources().generic();
            LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder((ServerWorld) getWorld()).add(LootContextParameters.THIS_ENTITY, this).add(LootContextParameters.ORIGIN, this.getPos()).add(LootContextParameters.DAMAGE_SOURCE, source).addOptional(LootContextParameters.KILLER_ENTITY, source.getSource()).addOptional(LootContextParameters.DIRECT_KILLER_ENTITY, source.getSource());
            if (player instanceof PlayerEntity) {
                builder.add(LootContextParameters.LAST_DAMAGE_PLAYER, (PlayerEntity) player).luck(((PlayerEntity) player).getLuck());
            }
            DisableVillagersMod.curedZombieLoot.generateLoot(builder.build(LootContextTypes.ENTITY), this::dropStack);
            this.kill();
            ci.cancel();
        }
    }
}
