package foundationgames.villagetweaker.api;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.*;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerType;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class FabricTrades {

    public static class BasicTradeFactory implements TradeOffers.Factory {
        private final ItemStack sell;
        private final ItemStack buy;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public BasicTradeFactory(Block sell, Item buy, int sellCount, int buyCount, int maxUses, int experience) {
            this(new ItemStack(sell), new ItemStack(buy), sellCount, buyCount, maxUses, experience);
        }

        public BasicTradeFactory(Item sell, Block buy, int sellCount, int buyCount, int maxUses, int experience) {
            this(new ItemStack(sell), new ItemStack(buy), sellCount, buyCount, maxUses, experience);
        }

        public BasicTradeFactory(Item sell, Item buy, int sellCount, int buyCount, int experience) {
            this(new ItemStack(sell), new ItemStack(buy), sellCount, buyCount, 12, experience);
        }

        public BasicTradeFactory(Item sell, Item buy, int sellCount, int buyCount, int maxUses, int experience) {
            this(new ItemStack(sell, sellCount), new ItemStack(buy, buyCount), sellCount, buyCount, maxUses, experience);
        }

        public BasicTradeFactory(ItemStack sell, ItemStack buy, int sellCount, int buyCount, int maxUses, int experience) {
            this(sell, buy, maxUses, experience, 0.05F);
        }

        public BasicTradeFactory(ItemStack sell, ItemStack buy, int maxUses, int experience, float multiplier) {
            this.sell = sell;
            this.buy = buy;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        public TradeOffer create(Entity entity, Random random) {
            return new TradeOffer(buy, sell, this.maxUses, this.experience, this.multiplier);
        }
    }

    public static class SellMapFactory implements TradeOffers.Factory {
        private final int price;
        private final String structure;
        private final MapIcon.Type iconType;
        private final int maxUses;
        private final int experience;

        public SellMapFactory(int price, String structure, MapIcon.Type iconType, int maxUses, int experience) {
            this.price = price;
            this.structure = structure;
            this.iconType = iconType;
            this.maxUses = maxUses;
            this.experience = experience;
        }

        public TradeOffer create(Entity entity, Random random) {
            if (!(entity.world instanceof ServerWorld)) {
                return null;
            } else {
                ServerWorld serverWorld = (ServerWorld)entity.world;
                BlockPos blockPos = serverWorld.locateStructure(this.structure, new BlockPos(entity), 100, true);
                if (blockPos != null) {
                    ItemStack itemStack = FilledMapItem.createMap(serverWorld, blockPos.getX(), blockPos.getZ(), (byte)2, true, true);
                    FilledMapItem.fillExplorationMap(serverWorld, itemStack);
                    MapState.addDecorationsTag(itemStack, blockPos, "+", this.iconType);
                    itemStack.setCustomName(new TranslatableText("filled_map." + this.structure.toLowerCase(Locale.ROOT), new Object[0]));
                    return new TradeOffer(new ItemStack(Items.EMERALD, this.price), new ItemStack(Items.COMPASS), itemStack, this.maxUses, this.experience, 0.2F);
                } else {
                    return null;
                }
            }
        }
    }

    public static class EnchantBookFactory implements TradeOffers.Factory {
        private final int experience;

        public EnchantBookFactory(int experience) {
            this.experience = experience;
        }

        public TradeOffer create(Entity entity, Random random) {
            Enchantment enchantment = Registry.ENCHANTMENT.getRandom(random);
            int i = MathHelper.nextInt(random, enchantment.getMinimumLevel(), enchantment.getMaximumLevel());
            ItemStack itemStack = EnchantedBookItem.forEnchantment(new InfoEnchantment(enchantment, i));
            int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
            if (enchantment.isTreasure()) {
                j *= 2;
            }

            if (j > 64) {
                j = 64;
            }

            return new TradeOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemStack, 12, this.experience, 0.2F);
        }
    }

    public static class SellDyedArmorFactory implements TradeOffers.Factory {
        private final Item sell;
        private final int price;
        private final int maxUses;
        private final int experience;

        public SellDyedArmorFactory(Item item, int price) {
            this(item, price, 12, 1);
        }

        public SellDyedArmorFactory(Item item, int price, int maxUses, int experience) {
            this.sell = item;
            this.price = price;
            this.maxUses = maxUses;
            this.experience = experience;
        }

        public TradeOffer create(Entity entity, Random random) {
            ItemStack itemStack = new ItemStack(Items.EMERALD, this.price);
            ItemStack itemStack2 = new ItemStack(this.sell);
            if (this.sell instanceof DyeableArmorItem) {
                List<DyeItem> list = Lists.newArrayList();
                list.add(getDye(random));
                if (random.nextFloat() > 0.7F) {
                    list.add(getDye(random));
                }

                if (random.nextFloat() > 0.8F) {
                    list.add(getDye(random));
                }

                itemStack2 = DyeableItem.blendAndSetColor(itemStack2, list);
            }

            return new TradeOffer(itemStack, itemStack2, this.maxUses, this.experience, 0.2F);
        }

        private static DyeItem getDye(Random random) {
            return DyeItem.byColor(DyeColor.byId(random.nextInt(16)));
        }
    }

    public static class SellPotionHoldingItemFactory implements TradeOffers.Factory {
        private final ItemStack sell;
        private final int sellCount;
        private final int price;
        private final int maxUses;
        private final int experience;
        private final Item secondBuy;
        private final int secondCount;
        private final float priceMultiplier;

        public SellPotionHoldingItemFactory(Item arrow, int secondCount, Item tippedArrow, int sellCount, int price, int maxUses, int experience) {
            this.sell = new ItemStack(tippedArrow);
            this.price = price;
            this.maxUses = maxUses;
            this.experience = experience;
            this.secondBuy = arrow;
            this.secondCount = secondCount;
            this.sellCount = sellCount;
            this.priceMultiplier = 0.05F;
        }

        public TradeOffer create(Entity entity, Random random) {
            ItemStack itemStack = new ItemStack(Items.EMERALD, this.price);
            List<Potion> list = Registry.POTION.stream().filter((potionx) -> !potionx.getEffects().isEmpty() && BrewingRecipeRegistry.isBrewable(potionx)).collect(Collectors.toList());
            Potion potion = list.get(random.nextInt(list.size()));
            ItemStack itemStack2 = PotionUtil.setPotion(new ItemStack(this.sell.getItem(), this.sellCount), potion);
            return new TradeOffer(itemStack, new ItemStack(this.secondBuy, this.secondCount), itemStack2, this.maxUses, this.experience, this.priceMultiplier);
        }
    }

    public static class SellEnchantedToolFactory implements TradeOffers.Factory {
        private final ItemStack tool;
        private final int basePrice;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public SellEnchantedToolFactory(Item tool, int basePrice, int maxUses, int experience) {
            this(tool, basePrice, maxUses, experience, 0.05F);
        }

        public SellEnchantedToolFactory(Item tool, int basePrice, int maxUses, int experience, float multiplier) {
            this.tool = new ItemStack(tool);
            this.basePrice = basePrice;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        public TradeOffer create(Entity entity, Random random) {
            int i = 5 + random.nextInt(15);
            ItemStack itemStack = EnchantmentHelper.enchant(random, new ItemStack(this.tool.getItem()), i, false);
            int j = Math.min(this.basePrice + i, 64);
            ItemStack itemStack2 = new ItemStack(Items.EMERALD, j);
            return new TradeOffer(itemStack2, itemStack, this.maxUses, this.experience, this.multiplier);
        }
    }

    public static class SellSuspiciousStewFactory implements TradeOffers.Factory {
        final StatusEffect effect;
        final int duration;
        final int experience;
        private final float multiplier;

        public SellSuspiciousStewFactory(StatusEffect statusEffect, int duration, int experience) {
            this.effect = statusEffect;
            this.duration = duration;
            this.experience = experience;
            this.multiplier = 0.05F;
        }

        public TradeOffer create(Entity entity, Random random) {
            ItemStack itemStack = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            SuspiciousStewItem.addEffectToStew(itemStack, this.effect, this.duration);
            return new TradeOffer(new ItemStack(Items.EMERALD, 1), itemStack, 12, this.experience, this.multiplier);
        }
    }

    public static class SellItemForEmeraldsFactory implements TradeOffers.Factory {
        private final ItemStack sell;
        private final int price;
        private final int count;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public SellItemForEmeraldsFactory(Block block, int price, int count, int maxUses, int experience) {
            this(new ItemStack(block), price, count, maxUses, experience);
        }

        public SellItemForEmeraldsFactory(Item item, int price, int count, int experience) {
            this((new ItemStack(item)), price, count, 12, experience);
        }

        public SellItemForEmeraldsFactory(Item item, int price, int count, int maxUses, int experience) {
            this(new ItemStack(item), price, count, maxUses, experience);
        }

        public SellItemForEmeraldsFactory(ItemStack itemStack, int price, int count, int maxUses, int experience) {
            this(itemStack, price, count, maxUses, experience, 0.05F);
        }

        public SellItemForEmeraldsFactory(ItemStack itemStack, int price, int count, int maxUses, int experience, float multiplier) {
            this.sell = itemStack;
            this.price = price;
            this.count = count;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        public TradeOffer create(Entity entity, Random random) {
            return new TradeOffer(new ItemStack(Items.EMERALD, this.price), new ItemStack(this.sell.getItem(), this.count), this.maxUses, this.experience, this.multiplier);
        }
    }

    public static class TypeAwareBuyForOneEmeraldFactory implements TradeOffers.Factory {
        private final Map<VillagerType, Item> map;
        private final ItemStack defaultItemStack;
        private final int count;
        private final int maxUses;
        private final int experience;

        public TypeAwareBuyForOneEmeraldFactory(int count, int maxUses, int experience, Map<VillagerType, Item> villagerTypeToItem, ItemStack defaultItemStack) {
            this.defaultItemStack = defaultItemStack;
            this.map = villagerTypeToItem;
            this.count = count;
            this.maxUses = maxUses;
            this.experience = experience;
        }
        public TradeOffer create(Entity entity, Random random) {
            if (entity instanceof VillagerDataContainer) {
                ItemStack stack = new ItemStack(this.map.getOrDefault(((VillagerDataContainer)entity).getVillagerData().getType(), Items.OAK_BOAT), this.count);
                return new TradeOffer(stack, new ItemStack(Items.EMERALD), this.maxUses, this.experience, 0.05F);
            } else {
                return null;
            }
        }
    }

    public static class BuyForOneEmeraldFactory implements TradeOffers.Factory {
        private final Item buy;
        private final int price;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public BuyForOneEmeraldFactory(ItemConvertible buy, int price, int maxUses, int experience) {
            this.buy = buy.asItem();
            this.price = price;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = 0.05F;
        }

        public TradeOffer create(Entity entity, Random random) {
            ItemStack itemStack = new ItemStack(this.buy, this.price);
            return new TradeOffer(itemStack, new ItemStack(Items.EMERALD), this.maxUses, this.experience, this.multiplier);
        }
    }
}
