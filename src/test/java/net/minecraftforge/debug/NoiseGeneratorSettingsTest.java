package net.minecraftforge.debug;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NoiseGeneratorSettingsTest.MOD_ID)
public class NoiseGeneratorSettingsTest {
    public static final String MOD_ID = "noise_generator_settings_test";
    private static final boolean ENABLED = true;

    public NoiseGeneratorSettingsTest() {
        if (!ENABLED)
            return;

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onGatherData);
    }

    public void onGatherData(GatherDataEvent event) {
        System.out.println("called");
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeServer(), new WorldGenData(generator.getPackOutput()));
    }

    static class WorldGenData extends DatapackBuiltinEntriesProvider {
        private static final ResourceKey<NoiseGeneratorSettings> TEST_SETTINGS = ResourceKey.create(Registries.NOISE_SETTINGS, new ResourceLocation(MOD_ID, "test_settings"));

        private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
                .add(Registries.NOISE_SETTINGS, (context) -> context.register(TEST_SETTINGS, NoiseGeneratorSettings.floatingIslands(context)));

        public WorldGenData(PackOutput output) {
            super(output, WorldGenData::createLookup);
        }

        private static HolderLookup.Provider createLookup() {
            return BUILDER.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), VanillaRegistries.createLookup());
        }
    }
}
