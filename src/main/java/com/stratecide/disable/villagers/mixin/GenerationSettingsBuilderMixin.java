package com.stratecide.disable.villagers.mixin;

import com.stratecide.disable.villagers.DisableVillagersMod;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GenerationSettings.Builder.class)
public class GenerationSettingsBuilderMixin {
    @Inject(method = "structureFeature", at = @At("HEAD"), cancellable = true)
    private void structureFeatureInject(ConfiguredStructureFeature<?, ?> structureFeature, CallbackInfoReturnable<GenerationSettings.Builder> cir) {
        if (DisableVillagersMod.getDisabledVillages() && StructureFeature.VILLAGE.equals(structureFeature.feature)) {
            Object self = (Object) this;
            cir.setReturnValue((GenerationSettings.Builder) self);
        }
    }
}
