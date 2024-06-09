package io.github.therookiecoder.snowyleavesplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.FoliageColors;

import static io.github.therookiecoder.snowyleavesplus.Snowiness.SNOWINESS;

public class SnowyLeavesPlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.BLOCK.register(
            (state, world, pos, index) -> {
                // Get the block's default colour
                int colour = getLeafBlockColour(state.getBlock(), world, pos);
                try {
                    return whiten(colour,
                        (double) state.get(SNOWINESS).ordinal() / Snowiness.values().length);
                // If there is any error, return the default colour
                } catch(Exception e) {
                    return colour;
                }
            },
            Blocks.OAK_LEAVES,
            Blocks.SPRUCE_LEAVES,
            Blocks.BIRCH_LEAVES,
            Blocks.JUNGLE_LEAVES,
            Blocks.ACACIA_LEAVES,
            Blocks.DARK_OAK_LEAVES,
            Blocks.MANGROVE_LEAVES,
            Blocks.AZALEA_LEAVES,
            Blocks.FLOWERING_AZALEA_LEAVES
        );
    }

    /**
     * Based on code from Minecraft's registered colour providers.
     * @see net.minecraft.client.color.block.BlockColors
     * @param block The block to get the colour of
     * @param world The current world
     * @param pos The position of the block
     * @return The leaf block's default colour.
     */
    private int getLeafBlockColour(Block block, BlockRenderView world, BlockPos pos) {
        if (block == Blocks.BIRCH_LEAVES) {
            return FoliageColors.getBirchColor();
        } else if (block == Blocks.SPRUCE_LEAVES) {
            return FoliageColors.getSpruceColor();
        } else if (block == Blocks.MANGROVE_LEAVES) {
            return FoliageColors.getMangroveColor();
        } else {
            return world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor();
        }
    }

    /**
     * Whiten a colour based on a whiteness value
     * @param colour The colour to modify
     * @param whiteness The amount to whiten the colour by.
     *                  A whiteness of <code>0</code> would return the same colour, and a
     *                  whiteness of <code>1</code> would return white (<code>0xffffff</code>)
     * @return The whitened colour
     */
    private int whiten(int colour, double whiteness) {
        // Extract colour channels
        int red = (colour >> 16 & 0xff);
        int green = (colour >> 8 & 0xff);
        int blue = (colour & 0xff);

        // Find out how far the colour is from white and interpolate based on the provided whiteness
        red += (0xff - red) * whiteness;
        green += (0xff - green) * whiteness;
        blue += (0xff - blue) * whiteness;

        // Combine the colours back
        return red << 16 | green << 8 | blue;
    }
}
