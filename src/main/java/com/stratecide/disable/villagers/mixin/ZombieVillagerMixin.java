package com.stratecide.disable.villagers.mixin;

import com.stratecide.disable.villagers.DisableVillagersMod;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(ZombieVillagerEntity.class)
public abstract class ZombieVillagerMixin extends ZombieEntity {

	@Shadow private UUID converter;

	public ZombieVillagerMixin(World world) {
		super(world);
	}

	@Inject(method = "interactMob", at=@At("HEAD"), cancellable = true)
	private void interactMobInject(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		if (!DisableVillagersMod.curableZombies) {
			ItemStack itemStack = player.getStackInHand(hand);
			if (itemStack.getItem() == Items.GOLDEN_APPLE) {
				cir.setReturnValue(ActionResult.PASS);
			}
		}
	}
}
