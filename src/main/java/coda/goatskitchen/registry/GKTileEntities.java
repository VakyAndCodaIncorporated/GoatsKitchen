package coda.goatskitchen.registry;

import coda.goatskitchen.GoatsKitchen;
import coda.goatskitchen.common.tileentities.BlenderTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GKTileEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, GoatsKitchen.MOD_ID);

    public static final RegistryObject<BlockEntityType<BlenderTileEntity>> BLENDER_TILE_ENTITY = REGISTER.register("blender", () -> BlockEntityType.Builder.of(BlenderTileEntity::new, GKBlocks.BLENDER.get()).build(null));
}