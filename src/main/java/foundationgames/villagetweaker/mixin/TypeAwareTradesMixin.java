package foundationgames.villagetweaker.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(TradeOffers.TypeAwareBuyForOneEmeraldFactory.class)
public class TypeAwareTradesMixin {
    private Map<VillagerType, Item> map;
    private int count;
    private int maxUses;
    private int experience;

    public TypeAwareTradesMixin(Map<VillagerType, Item> map, int count, int maxUses, int experience) {
        this.map = map;
        this.count = count;
        this.maxUses = maxUses;
        this.experience = experience;
    }

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/village/TradeOffers$TypeAwareBuyForOneEmeraldFactory"))
    private void init(int i, int j, int experience, Map<VillagerType, Item> map) {
        Registry.VILLAGER_TYPE.stream().filter((villagerType) -> !map.containsKey(villagerType)).findAny().ifPresent((villagerType) -> {
            for(VillagerType type : Registry.VILLAGER_TYPE) {
                if(!map.containsKey(type)) {
                    map.put(type, Items.STICK);
                }
            }
        });
        this.map = map;
        this.count = i;
        this.maxUses = j;
        this.experience = experience;
    }
}
