package com.stratecide.disable.villagers.mixin;

import java.util.Map;
import java.util.Optional;

import com.stratecide.disable.villagers.DisableVillagersMod;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.gen.chunk.StrongholdConfig;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.feature.StructureFeature;

@Mixin(StructuresConfig.class)
public class StructuresConfigMixin {
    @Inject(method = "<init>(Ljava/util/Optional;Ljava/util/Map;)V", at = @At("TAIL"))
    void injectConstructor(Optional<StrongholdConfig> stronghold, Map<StructureFeature<?>, StructureConfig> structures, CallbackInfo ci) {
        if (DisableVillagersMod.getDisabledVillages()) {
            structures.remove(StructureFeature.VILLAGE);
        }
    }

}
