package com.example.mixin;

import com.example.GravityDuplicationReturn;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class PortalDuplicationMixin {
    @Shadow public abstract Level level();
    @Shadow public abstract double getX();
    @Shadow public abstract double getY();
    @Shadow public abstract double getZ();

    @Inject(method = "setRemoved", at = @At("HEAD"), cancellable = true)
    private void onSetRemoved(Entity.RemovalReason reason, CallbackInfo ci) {
        if (reason == Entity.RemovalReason.CHANGED_DIMENSION) {
            Entity entity = (Entity) (Object) this;
            if (entity.level() instanceof ServerLevel serverLevel) {
                boolean duplicatable = entity instanceof FallingBlockEntity || 
                        entity.getType().builtInRegistryHolder().is(GravityDuplicationReturn.DUPLICATABLE_ENTITIES);

                if (duplicatable) {
                    boolean enabled = GravityDuplicationReturn.ENABLE_PORTAL_DUPLICATION == null ||
                            serverLevel.getGameRules().get(GravityDuplicationReturn.ENABLE_PORTAL_DUPLICATION);

                    if (enabled) {
                        if (entity instanceof FallingBlockEntity fallingBlock) {
                            boolean isDragonEgg = fallingBlock.getBlockState().is(Blocks.DRAGON_EGG);
                            boolean eggEnabled = GravityDuplicationReturn.ENABLE_DRAGON_EGG_DUPLICATION == null ||
                                    serverLevel.getGameRules().get(GravityDuplicationReturn.ENABLE_DRAGON_EGG_DUPLICATION);

                            if (isDragonEgg && !eggEnabled) {
                                return;
                            }
                        }

                        ci.cancel();

                        String name = entity.getType().getDescription().getString();
                        if (entity instanceof FallingBlockEntity fallingBlock) {
                            try {
                                name = fallingBlock.getBlockState().getBlock().getName().getString();
                            } catch (Exception ignored) {}
                        }

                        GravityDuplicationReturn.LOGGER.info("[GravityDupe] {} duplicated at End Portal at [x={}, y={}, z={}]",
                                name, (int)this.getX(), (int)this.getY(), (int)this.getZ());
                    }
                }
            }
        }
    }
}
