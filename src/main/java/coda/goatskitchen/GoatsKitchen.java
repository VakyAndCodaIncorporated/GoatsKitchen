package coda.goatskitchen;

import coda.goatskitchen.common.entities.ChefEntity;
import coda.goatskitchen.common.entities.LonghornEntity;
import coda.goatskitchen.common.init.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GoatsKitchen.MOD_ID)
public class GoatsKitchen {
    public static final String MOD_ID = "goatskitchen";

    public final static CreativeModeTab GROUP = new CreativeModeTab(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(GKBlocks.BLENDER.get());
        }
    };

    public GoatsKitchen() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        bus.addListener(this::registerEntityAttributes);
        forgeBus.addListener(this::onBiomeLoad);

        GKBlocks.REGISTER.register(bus);
        GKTileEntities.REGISTER.register(bus);
        GKItems.REGISTER.register(bus);
        GKEntities.REGISTER.register(bus);
        GKContainers.REGISTER.register(bus);
        GKFeatures.REGISTER.register(bus);
        GKRecipes.REGISTER.register(bus);

    }

    private void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(GKEntities.CHEF.get(), ChefEntity.createAttributes().build());
        event.put(GKEntities.LONGHORN.get(), LonghornEntity.createAttributes().build());
    }

    private void onBiomeLoad(BiomeLoadingEvent event) {
        ResourceLocation name = event.getName();

        if (event.getName().toString().equals("minecraft:shattered_savanna")) {
            event.getGeneration().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION).add(() -> GKFeatures.PATCH_PINEAPPLE);
        }
    }
}