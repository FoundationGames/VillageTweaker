package foundationgames.villagetweaker;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import foundationgames.villagetweaker.api.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biomes;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;

public class VillageTweaker implements ModInitializer {

    //public static final FabricPointOfInterestType EXAMPLE_POI = new FabricPointOfInterestType(
    //    new Identifier("example", "example"),
    //    Blocks.BLACK_WOOL,
    //    1, 1
    //);
    //public static final FabricVillagerProfession EXAMPLE_VILLAGER = new FabricVillagerProfession(
    //    new Identifier("example", "example"),
    //    EXAMPLE_POI,
    //   ImmutableSet.of(),
    //    ImmutableSet.of(),
    //    SoundEvents.ENTITY_SHEEP_AMBIENT
    //);

    @Override
    public void onInitialize() {
        Map<VillagerType, Item> map = Maps.newHashMap();
            map.put(VillagerType.PLAINS, Items.OAK_BOAT);
            map.put(VillagerType.TAIGA, Items.SPRUCE_BOAT);
            map.put(VillagerType.SNOW, Items.SPRUCE_BOAT);
            map.put(VillagerType.DESERT, Items.JUNGLE_BOAT);
            map.put(VillagerType.JUNGLE, Items.JUNGLE_BOAT);
            map.put(VillagerType.SAVANNA, Items.ACACIA_BOAT);
            map.put(VillagerType.SWAMP, Items.DARK_OAK_BOAT);
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(VillagerProfession.FISHERMAN).merge(1, new TradeOffers.Factory[]{
                new FabricTrades.TypeAwareBuyForOneEmeraldFactory(1, 10, 2, map, new ItemStack(Items.OAK_BOAT, 1))
        }, ArrayUtils::addAll);
        /*------------------------*/
        //FabricVillagerType.createVillagerType(new Identifier("example", "example"), Biomes.FLOWER_FOREST);
        //VillageTweaker.registerPOI(EXAMPLE_POI);
        //VillageTweaker.registerProfession(EXAMPLE_VILLAGER);
        //VillageTweaker.addStructureToPool(
        //        "minecraft:village/plains/houses", //pool to add to
        //        "minecraft:village/snowy/houses/snowy_fisher_cottage",  //your structure
        //        25 //weight
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
