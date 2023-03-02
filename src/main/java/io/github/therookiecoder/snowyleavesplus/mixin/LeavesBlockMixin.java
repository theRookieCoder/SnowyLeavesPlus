package io.github.therookiecoder.snowyleavesplus.mixin;

import io.github.therookiecoder.snowyleavesplus.Snowiness;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.therookiecoder.snowyleavesplus.Snowiness.SNOWINESS;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {
    private boolean isLeavesBlock() {
        return this.getClass().equals(LeavesBlock.class);
    }

    // Add the snowiness property to leaf blocks
    @Inject(method = "appendProperties", at = @At("TAIL"))
    private void appendPropertiesInject(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        if (!isLeavesBlock()) return;
        builder.add(SNOWINESS);
    }

    // Set the default snowiness to none
    @Inject(method = "<init>", at = @At("TAIL"))
    private void initInject(AbstractBlock.Settings settings, CallbackInfo ci) {
        if (!isLeavesBlock()) return;
        ((BlockInvoker) this)
            .invokeSetDefaultState(
                ((LeavesBlock)(Object) this)
                    .getDefaultState().with(
                        SNOWINESS,
                        Snowiness.none
                    )
            );
    }

    // Always randomly tick leaf blocks
    @Inject(method = "hasRandomTicks", at = @At("RETURN"), cancellable = true)
    private void hasRandomTicksInject(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (isLeavesBlock()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void randomTickInject(
        BlockState state,
        ServerWorld world,
        BlockPos pos,
        Random random,
        CallbackInfo ci
    ) {
        if (!isLeavesBlock()) return;
        Snowiness currentSnowiness = state.get(SNOWINESS);
        if (
            // If it's snowing
            world.isRaining()
            && world.getBiome(pos).value().getPrecipitation(pos) == Biome.Precipitation.SNOW
        ) {
            // And the block is at least somewhat exposed to the sky
            if (world.getLightLevel(LightType.SKY, pos) > 10) {
                // Make it more snowy
                world.setBlockState(pos, state.with(SNOWINESS, currentSnowiness.increaseSnowiness()));
            }
        } else {
            // Else make it less snowy
            world.setBlockState(pos, state.with(SNOWINESS, currentSnowiness.decreaseSnowiness()));
        }
    }
}
