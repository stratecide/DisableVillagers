package com.stratecide.disable.villagers.mixin;

import com.stratecide.disable.villagers.DisableVillagersMod;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.village.TradeOfferList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MerchantEntity.class)
public class MerchantEntityMixin {

    @Inject(method = "getOffers", at = @At("HEAD"), cancellable = true)
    private void injectGetOffers(CallbackInfoReturnable<TradeOfferList> cir) {
        if (DisableVillagersMod.blockTrading && ((Object) this) instanceof VillagerEntity
        || DisableVillagersMod.disableWanderingTrader && ((Object) this) instanceof WanderingTraderEntity) {
            cir.setReturnValue(new TradeOfferList());
        }
    }
}
