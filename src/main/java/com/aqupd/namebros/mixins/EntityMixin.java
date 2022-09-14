package com.aqupd.namebros.mixins;

import com.aqupd.namebros.Config;
import com.aqupd.namebros.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow @Nullable public abstract Text getCustomName();
    @Shadow public abstract Text getName();

    @Shadow public abstract EntityType<?> getType();

    @Inject(
        method = "isTeammate(Lnet/minecraft/entity/Entity;)Z",
        at = @At("TAIL"),
        cancellable = true
    )
    private void isTeammate(Entity other, CallbackInfoReturnable<Boolean> cir) {
        Config config = Config.INSTANCE;
        if(config.isDebug()) Main.LOGGER.info(this.getType().getUntranslatedName() + " " + other.getType().getUntranslatedName());

        if(config.enabled() && !config.getBlacklist().contains(this.getType().getUntranslatedName()) && !config.getBlacklist().contains(other.getType().getUntranslatedName())) {
            if (this.getCustomName() != null && other.getName() != null && this.getCustomName().getString().equals(other.getName().getString()))
                cir.setReturnValue(true);
            if (this.getCustomName() != null && other.getCustomName() != null && this.getCustomName().getString().equals(other.getCustomName().getString()))
                cir.setReturnValue(true);
        }
    }
}
