package com.stratecide.disable.villagers.mixin;

import com.stratecide.disable.villagers.DisableVillagersMod;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin {
    /**
     * Replace zombie villagers by adding their spawn-weight to the normal zombies'
     */
    @ModifyVariable(method = "addMonsters", at = @At("HEAD"), ordinal = 0)
    private static int fixZombieChance(int weight, SpawnSettings.Builder builder, int zombieWeight, int zombieVillagerWeight, int skeletonWeight) {
        if (DisableVillagersMod.getDisabledZombies())
            return weight + zombieVillagerWeight;
        return weight;
    }

    /**
     * bypass the builder.spawn(...) call if zombie villagers are disabled
     */
    @Redirect(method = "addMonsters", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/SpawnSettings$Builder;spawn(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/world/biome/SpawnSettings$SpawnEntry;)Lnet/minecraft/world/biome/SpawnSettings$Builder;", ordinal = 2))
    private static SpawnSettings.Builder removeZombieVillagers(SpawnSettings.Builder builder, SpawnGroup spawnGroup, SpawnSettings.SpawnEntry spawnEntry) {
        if (!DisableVillagersMod.getDisabledZombies())
            return builder.spawn(spawnGroup, spawnEntry);
        return builder;
    }
}
