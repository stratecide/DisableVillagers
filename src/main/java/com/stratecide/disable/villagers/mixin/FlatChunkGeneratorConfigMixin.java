package com.stratecide.disable.villagers.mixin;

import java.util.Optional;
import java.util.stream.Collectors;

import com.stratecide.disable.villagers.DisableVillagersMod;
import net.minecraft.structure.StructureSetKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.structure.StructureSet;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;

@Mixin(FlatChunkGeneratorConfig.class)
public class FlatChunkGeneratorConfigMixin {
    @ModifyVariable(method = "<init>(Ljava/util/Optional;Lnet/minecraft/util/registry/Registry;)V", at = @At("HEAD"), index = 1, argsOnly = true)
    private static Optional<RegistryEntryList<StructureSet>> removeVillages(Optional<RegistryEntryList<StructureSet>> value) {
        if (DisableVillagersMod.getDisabledVillages()) {
            return value.map(registryEntries ->
                    RegistryEntryList.of(
                            registryEntries.stream()
                                    .filter(structureSetRegistryEntry -> !structureSetRegistryEntry.matchesKey(StructureSetKeys.VILLAGES))
                                    .collect(Collectors.toList())
                    )
            );
        } else{
            return value;
        }
    }
}
