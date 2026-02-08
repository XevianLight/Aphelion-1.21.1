package net.xevianlight.aphelion.block.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class RocketSeat extends Block {
    public RocketSeat(Properties properties) {
        super(properties);
    }

    public static Properties getProperties() {
        return Properties
                .of()
                .sound(SoundType.STONE)
                .destroyTime(2f)
                .explosionResistance(10f)
                .requiresCorrectToolForDrops();
    }

    public static Item.Properties getItemProperties() {
        return new Item.Properties();
    }
}
