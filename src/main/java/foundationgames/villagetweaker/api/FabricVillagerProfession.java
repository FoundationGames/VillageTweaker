package foundationgames.villagetweaker.api;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;

public class FabricVillagerProfession {
    private VillagerProfession underlying;
    private Identifier id;

    public FabricVillagerProfession(Identifier identifier, FabricPointOfInterestType workStation, ImmutableSet<Item> gatherableItems, ImmutableSet<Block> secondaryJobSites, SoundEvent workSound) {
        underlying = new VillagerProfession(identifier.toString(), workStation.getUnderlying(), gatherableItems, secondaryJobSites, workSound);
        id = identifier;
    }

    public VillagerProfession getUnderlying() {
        return underlying;
    }

    public Identifier getId() {
        return id;
    }
}
