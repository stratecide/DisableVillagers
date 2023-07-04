package com.stratecide.disable.villagers.mixin;

import com.google.common.collect.ImmutableList;
import com.stratecide.disable.villagers.DisableVillagersMod;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureSetKeys;
import net.minecraft.structure.StructureSets;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(StructureSets.class)
public interface StructureSetsMixin {

    @Inject(method = "register(Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/structure/StructureSet;)Lnet/minecraft/util/registry/RegistryEntry;", at = @At("HEAD"), cancellable = true)
    private static void blockVillages(RegistryKey<StructureSet> key, StructureSet structureSet, CallbackInfoReturnable<RegistryEntry<StructureSet>> cir) {
        if (DisableVillagersMod.getDisabledVillages() && StructureSetKeys.VILLAGES.equals(key)) {
            cir.setReturnValue(BuiltinRegistries.add(BuiltinRegistries.STRUCTURE_SET, key, new StructureSet(new ArrayList<>(), structureSet.placement())));
        }
    }
}
