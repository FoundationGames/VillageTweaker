package foundationgames.villagetweaker.api;

import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biome;

public class FabricVillagerType {
    private VillagerType underlying;
    private Identifier id;

    public static FabricVillagerType createVillagerType(Identifier id, Biome biome) {
        return new FabricVillagerType(id, biome);
    }

    private FabricVillagerType(Identifier id, Biome biome) {
        underlying = VillagerType.create(id.toString());
        VillagerType.BIOME_TO_TYPE.put(biome, underlying);
    }

    public VillagerType getUnderlying() {
        return underlying;
    }

    public Identifier getId() {
        return id;
    }
}
