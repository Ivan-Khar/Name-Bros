package com.aqupd.namebros.mixins;

import net.minecraft.entity.Entity;
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

    @Inject(
        method = "isTeammate(Lnet/minecraft/entity/Entity;)Z",
        at = @At("TAIL"),
        cancellable = true
    )
    private void isSameNames(Entity other, CallbackInfoReturnable<Boolean> cir) {
        if(this.getCustomName() != null && other.getName() != null && this.getCustomName().getString().equals(other.getName().getString())) cir.setReturnValue(true);
        if(this.getCustomName() != null && other.getCustomName() != null && this.getCustomName().getString().equals(other.getCustomName().getString())) cir.setReturnValue(true);
    }
}
