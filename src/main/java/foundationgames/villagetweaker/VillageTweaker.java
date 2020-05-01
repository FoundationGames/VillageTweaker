package foundationgames.villagetweaker;

import com.google.common.collect.ImmutableSet;
import foundationgames.villagetweaker.api.FabricPointOfInterestType;
import foundationgames.villagetweaker.api.FabricVillagerProfession;
import foundationgames.villagetweaker.api.FabricVillagerType;
import foundationgames.villagetweaker.api.StructurePoolAddCallback;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biomes;

public class VillageTweaker implements ModInitializer {

    //public static final FabricPointOfInterestType EXAMPLE_POI = new FabricPointOfInterestType(
    //    new Identifier("example", "example"),
    //    Blocks.BLACK_WOOL,
    //    1, 1
    //);
    //public static final FabricVillagerProfession EXAMPLE_VILLAGER = new FabricVillagerProfession(
    //    new Identifier("example", "example"),
    //    EXAMPLE_POI,
    //    ImmutableSet.of(),
    //    ImmutableSet.of(),
    //    SoundEvents.ENTITY_SHEEP_AMBIENT
    //);

    @Override
    public void onInitialize() {
        //VillageTweaker.registerPOI(EXAMPLE_POI);
        //VillageTweaker.registerProfession(EXAMPLE_VILLAGER);
        //VillageTweaker.addStructureToPool(
                //"minecraft:village/plains/houses", //pool to add to
                //"minecraft:village/snowy/houses/snowy_fisher_cottage",  //your structure
                //25 //weight
        //);
    }

    public static void addStructureToPool(String poolID, String elementID, int weight) {
        StructurePoolAddCallback.EVENT.register(structurePool -> {
            if(structurePool.getUnderlying().getId().toString().equals(poolID)) {
                structurePool.addStructurePoolElement(new SinglePoolElement(elementID), weight);
            }
        });
    }
    public static void registerProfession(FabricVillagerProfession profession) {
        Registry.register(Registry.VILLAGER_PROFESSION, profession.getId(), profession.getUnderlying());
    }
    public static void registerPOI(FabricPointOfInterestType poi) {
        Registry.register(Registry.POINT_OF_INTEREST_TYPE, poi.getId(), poi.getUnderlying());
    }
}
