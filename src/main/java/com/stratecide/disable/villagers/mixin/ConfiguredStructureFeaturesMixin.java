package com.stratecide.disable.villagers.mixin;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.StructureFeature;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

import com.stratecide.disable.villagers.DisableVillagersMod;

@Mixin(ConfiguredStructureFeatures.class)
public class ConfiguredStructureFeaturesMixin {
    @Inject(method = "register(Ljava/util/function/BiConsumer;Lnet/minecraft/world/gen/feature/ConfiguredStructureFeature;Lnet/minecraft/util/registry/RegistryKey;)V", at = @At("HEAD"), cancellable = true)
    private static void injectStructionRegistration(BiConsumer<ConfiguredStructureFeature<?, ?>, RegistryKey<Biome>> registrar, ConfiguredStructureFeature<?, ?> feature, RegistryKey<Biome> biome, CallbackInfo ci) {
        if (StructureFeature.VILLAGE.equals(feature.feature) && DisableVillagersMod.getDisabledVillages()) {
            ci.cancel();
        }
    }
}
