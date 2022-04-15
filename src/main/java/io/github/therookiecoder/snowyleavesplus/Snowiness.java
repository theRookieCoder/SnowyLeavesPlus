package io.github.therookiecoder.snowyleavesplus;

import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;

public enum Snowiness implements StringIdentifiable {
    none,
    low,
    medium,
    high,
    full;

    public static final EnumProperty<Snowiness> SNOWINESS = EnumProperty.of("snowiness", Snowiness.class);

    @Override
    public String asString() {
        return this.name();
    }

    /**
     * Increment snowiness by one.
     * Returns the same value if it is already at the maximum level
     * @return The current snowiness value increased by one
     */
    public Snowiness increaseSnowiness() {
        if (this.ordinal() + 1 < Snowiness.values().length) {
            return Snowiness.values()[this.ordinal() + 1];
        } else {
            return this;
        }
    }

    /**
     * Decrement snowiness by one.
     * Returns the same value if it is already at the minimum level
     * @return The current snowiness value decreased by one
     */
    public Snowiness decreaseSnowiness() {
        if (this.ordinal() > 0) {
            return Snowiness.values()[this.ordinal() - 1];
        } else {
            return this;
        }
    }
}
