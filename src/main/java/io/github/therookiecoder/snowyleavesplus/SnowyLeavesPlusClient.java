package io.github.therookiecoder.snowyleavesplus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

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
            Blocks.CHERRY_LEAVES,
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
        } else if (block == Blocks.CHERRY_LEAVES) {
            // use of placeholder (#f09bc8) when using experimental features
            // TODO: use FoliageColors.getCherryColor() when available
            return 16566245;
        } else if (
            block == Blocks.AZALEA_LEAVES
                || block == Blocks.FLOWERING_AZALEA_LEAVES) {
            // TODO: find a specific colour if applicable
            return world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor();
        } else if (
            block == Blocks.OAK_LEAVES
                || block == Blocks.JUNGLE_LEAVES
                || block == Blocks.ACACIA_LEAVES
                || block == Blocks.DARK_OAK_LEAVES
                || block == Blocks.MANGROVE_LEAVES) {
            return world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor();
        }
        else {
            // TODO: Actually get the default colour
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
