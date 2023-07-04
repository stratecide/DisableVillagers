package com.stratecide.disable.villagers.mixin;

import com.stratecide.disable.villagers.DisableVillagersMod;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;
import net.minecraft.world.gen.structure.Structures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Structures.class)
public abstract class StructuresMixin {

    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static void blockVillages(RegistryKey<Structure> key, Structure structure, CallbackInfoReturnable<RegistryEntry<Structure>> cir) {
        if (DisableVillagersMod.getDisabledVillages() && (StructureKeys.VILLAGE_DESERT.equals(key)
        || StructureKeys.VILLAGE_PLAINS.equals(key)
        || StructureKeys.VILLAGE_DESERT.equals(key)
        || StructureKeys.VILLAGE_SAVANNA.equals(key)
        || StructureKeys.VILLAGE_SNOWY.equals(key)
        || StructureKeys.VILLAGE_TAIGA.equals(key))) {
            cir.setReturnValue(null);
        }
    }
}
