package com.stratecide.disable.villagers.mixin;

import com.stratecide.disable.villagers.DisableVillagersMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
}
