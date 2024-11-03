/*
 * Copyright © Wynntils 2024.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package com.wynntils.mc.mixin;

import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LightTexture.class)
public class LightTextureMixin {
    // FIXME: Port to 1.21.3
    //    @WrapOperation(
    //            method = "updateLightTexture",
    //            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/NativeImage;setPixelRGBA(III)V"))
    //    private void updateLightmapRGB(NativeImage image, int x, int y, int rgb, Operation<Void> original) {
    //        final LightmapEvent lightmapEvent = new LightmapEvent(rgb);
    //        MixinHelper.post(lightmapEvent);
    //        original.call(image, x, y, lightmapEvent.getRgb());
    //    }
}
