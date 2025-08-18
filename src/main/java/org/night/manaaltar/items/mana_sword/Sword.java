package org.night.manaaltar.items.mana_sword;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class Sword extends SwordItem {

    private static final Tier MANA_TIER = new Tier() {
        @Override public int getUses() { return 1561; }
        @Override public float getSpeed() { return 0f; }
        @Override public float getAttackDamageBonus() { return 0f; }
        @Override public int getEnchantmentValue() { return 10; }
        @Override public Ingredient getRepairIngredient() { return Ingredient.EMPTY; }
        @Override public TagKey<Block> getIncorrectBlocksForDrops() {
            return BlockTags.INCORRECT_FOR_STONE_TOOL;
        }
    };

    public Sword(Item.Properties props) {
        super(MANA_TIER,
                props
                        .stacksTo(1)
                        .attributes(SwordItem.createAttributes(MANA_TIER, 8.0f, -2.4f))
        );
    }
}
