package foundationgames.villagetweaker.api;

import net.minecraft.item.BoatItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public enum WoodType {
    OAK(Items.OAK_BOAT),
    BIRCH(Items.BIRCH_BOAT),
    SPRUCE(Items.SPRUCE_BOAT),
    ACACIA(Items.ACACIA_BOAT),
    JUNGLE(Items.JUNGLE_BOAT),
    DARK_OAK(Items.DARK_OAK_BOAT);

    private BoatItem boatItem;

    WoodType(Item boatItem) {
        this.boatItem = (BoatItem)boatItem;
    }
}
