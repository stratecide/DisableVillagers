package com.stratecide.disable.villagers.mixin;

import com.stratecide.disable.villagers.DisableVillagersMod;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureSetKeys;
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.stream.Collector;
import java.util.stream.Stream;

@Mixin(StructurePlacementCalculator.class)
public class StructurePlacementCalculatorMixin {
    @ModifyVariable(method = "create(Lnet/minecraft/world/gen/noise/NoiseConfig;JLnet/minecraft/world/biome/source/BiomeSource;Ljava/util/stream/Stream;)Lnet/minecraft/world/gen/chunk/placement/StructurePlacementCalculator;", at = @At("HEAD"), argsOnly = true)
    private static Stream<RegistryEntry<StructureSet>> removeVillages1(Stream<RegistryEntry<StructureSet>> structureSets) {
        if (DisableVillagersMod.getDisabledVillages()) {
            return structureSets.filter(entry ->
                    !entry.matchesKey(StructureSetKeys.VILLAGES)
            );
        } else {
            return structureSets;
        }
    }

    @Redirect(method = "create(Lnet/minecraft/world/gen/noise/NoiseConfig;JLnet/minecraft/world/biome/source/BiomeSource;Lnet/minecraft/registry/RegistryWrapper;)Lnet/minecraft/world/gen/chunk/placement/StructurePlacementCalculator;", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;collect(Ljava/util/stream/Collector;)Ljava/lang/Object;"))
    private static Object removeVillages2(Stream<RegistryEntry<StructureSet>> stream, Collector collector) {
        return removeVillages1(stream).collect(collector);
    }
}
