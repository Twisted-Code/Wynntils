/*
 * Copyright © Wynntils 2022.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.mc.utils;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.wynntils.utils.objects.CustomColor;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public class RenderUtils {

    public static final ResourceLocation highlight = new ResourceLocation("wynntils", "textures/highlight.png");

    public static void drawRect(CustomColor color, int x, int y, int z, int width, int height) {
        drawRect(new PoseStack(), color, x, y, z, width, height);
    }

    public static void drawRect(PoseStack poseStack, CustomColor color, int x, int y, int z, int width, int height) {
        int alpha = color.a & 0xFF;
        int red = color.r & 0xFF;
        int green = color.g & 0xFF;
        int blue = color.b & 0xFF;

        Matrix4f matrix = poseStack.last().pose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder
                .vertex(matrix, x, y + height, z)
                .color(red, green, blue, alpha)
                .endVertex();
        bufferBuilder
                .vertex(matrix, x + width, y + height, z)
                .color(red, green, blue, alpha)
                .endVertex();
        bufferBuilder
                .vertex(matrix, x + width, y, z)
                .color(red, green, blue, alpha)
                .endVertex();
        bufferBuilder.vertex(matrix, x, y, z).color(red, green, blue, alpha).endVertex();
        bufferBuilder.end();
        BufferUploader.end(bufferBuilder);
        RenderSystem.disableBlend();
    }

    public static void drawTexturedRect(
            ResourceLocation tex, int x, int y, int width, int height, int textureWidth, int textureHeight) {
        drawTexturedRect(
                new PoseStack(), tex, x, y, 0, width, height, 0, 0, width, height, textureWidth, textureHeight);
    }

    public static void drawTexturedRect(
            PoseStack poseStack,
            ResourceLocation tex,
            int x,
            int y,
            int z,
            int width,
            int height,
            int uOffset,
            int vOffset,
            int u,
            int v,
            int textureWidth,
            int textureHeight) {
        float uScale = 1f / textureWidth;
        float vScale = 1f / textureHeight;

        Matrix4f matrix = poseStack.last().pose();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, tex);
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder
                .vertex(matrix, x, y + height, z)
                .uv(uOffset * uScale, (vOffset + v) * vScale)
                .endVertex();
        bufferBuilder
                .vertex(matrix, x + width, y + height, z)
                .uv((uOffset + u) * uScale, (vOffset + v) * vScale)
                .endVertex();
        bufferBuilder
                .vertex(matrix, x + width, y, z)
                .uv((uOffset + u) * uScale, vOffset * vScale)
                .endVertex();
        bufferBuilder
                .vertex(matrix, x, y, z)
                .uv(uOffset * uScale, vOffset * vScale)
                .endVertex();
        bufferBuilder.end();
        BufferUploader.end(bufferBuilder);
    }

    public static void drawTexturedRectWithColor(
            ResourceLocation tex,
            CustomColor color,
            int x,
            int y,
            int z,
            int width,
            int height,
            int textureWidth,
            int textureHeight) {
        drawTexturedRectWithColor(
                new PoseStack(), tex, color, x, y, z, width, height, 0, 0, width, height, textureWidth, textureHeight);
    }

    public static void drawTexturedRectWithColor(
            PoseStack poseStack,
            ResourceLocation tex,
            CustomColor color,
            int x,
            int y,
            int z,
            int width,
            int height,
            int uOffset,
            int vOffset,
            int u,
            int v,
            int textureWidth,
            int textureHeight) {
        int alpha = color.a & 0xFF;
        int red = color.r & 0xFF;
        int green = color.g & 0xFF;
        int blue = color.b & 0xFF;

        float uScale = 1f / textureWidth;
        float vScale = 1f / textureHeight;

        Matrix4f matrix = poseStack.last().pose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, tex);
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder
                .vertex(matrix, x, y + height, z)
                .uv(uOffset * uScale, (vOffset + v) * vScale)
                .color(red, green, blue, alpha)
                .endVertex();
        bufferBuilder
                .vertex(matrix, x + width, y + height, z)
                .uv((uOffset + u) * uScale, (vOffset + v) * vScale)
                .color(red, green, blue, alpha)
                .endVertex();
        bufferBuilder
                .vertex(matrix, x + width, y, z)
                .uv((uOffset + u) * uScale, vOffset * vScale)
                .color(red, green, blue, alpha)
                .endVertex();
        bufferBuilder
                .vertex(matrix, x, y, z)
                .uv(uOffset * uScale, vOffset * vScale)
                .color(red, green, blue, alpha)
                .endVertex();
        bufferBuilder.end();
        BufferUploader.end(bufferBuilder);
        RenderSystem.disableBlend();
    }

    public static void fillGradient(
            Matrix4f matrix,
            BufferBuilder builder,
            int x1,
            int y1,
            int x2,
            int y2,
            int blitOffset,
            CustomColor colorA,
            CustomColor colorB) {
        int A_a = (colorA.a & 0xFF);
        int A_r = (colorA.r & 0xFF);
        int A_g = (colorA.g & 0xFF);
        int A_b = (colorA.b & 0xFF);
        int B_a = (colorB.a & 0xFF);
        int B_r = (colorB.r & 0xFF);
        int B_g = (colorB.g & 0xFF);
        int B_b = (colorB.b & 0xFF);
        builder.vertex(matrix, x2, y1, blitOffset).color(A_r, A_g, A_b, A_a).endVertex();
        builder.vertex(matrix, x1, y1, blitOffset).color(A_r, A_g, A_b, A_a).endVertex();
        builder.vertex(matrix, x1, y2, blitOffset).color(B_r, B_g, B_b, B_a).endVertex();
        builder.vertex(matrix, x2, y2, blitOffset).color(B_r, B_g, B_b, B_a).endVertex();
    }

    public static void drawTooltip(List<ClientTooltipComponent> lines, PoseStack poseStack, Font font) {
        int tooltipWidth = 0;
        int tooltipHeight = lines.size() == 1 ? -2 : 0;

        for (ClientTooltipComponent clientTooltipComponent : lines) {
            int lineWidth = clientTooltipComponent.getWidth(font);
            if (lineWidth > tooltipWidth) {
                tooltipWidth = lineWidth;
            }
            tooltipHeight += clientTooltipComponent.getHeight();
        }

        // background box
        poseStack.pushPose();
        int tooltipX = 4;
        int tooltipY = 4;
        // somewhat hacky solution to get around transparency issues - these colors were chosen to best match
        // how tooltips are displayed in-game
        CustomColor backgroundColor = CustomColor.fromInt(0xFF100010);
        CustomColor borderColorStart = CustomColor.fromInt(0xFF25005B);
        CustomColor borderColorEnd = CustomColor.fromInt(0xFF180033);
        int zLevel = 400;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix4f = poseStack.last().pose();
        fillGradient(
                matrix4f,
                bufferBuilder,
                tooltipX - 3,
                tooltipY - 4,
                tooltipX + tooltipWidth + 3,
                tooltipY - 3,
                zLevel,
                backgroundColor,
                backgroundColor);
        fillGradient(
                matrix4f,
                bufferBuilder,
                tooltipX - 3,
                tooltipY + tooltipHeight + 3,
                tooltipX + tooltipWidth + 3,
                tooltipY + tooltipHeight + 4,
                zLevel,
                backgroundColor,
                backgroundColor);
        fillGradient(
                matrix4f,
                bufferBuilder,
                tooltipX - 3,
                tooltipY - 3,
                tooltipX + tooltipWidth + 3,
                tooltipY + tooltipHeight + 3,
                zLevel,
                backgroundColor,
                backgroundColor);
        fillGradient(
                matrix4f,
                bufferBuilder,
                tooltipX - 4,
                tooltipY - 3,
                tooltipX - 3,
                tooltipY + tooltipHeight + 3,
                zLevel,
                backgroundColor,
                backgroundColor);
        fillGradient(
                matrix4f,
                bufferBuilder,
                tooltipX + tooltipWidth + 3,
                tooltipY - 3,
                tooltipX + tooltipWidth + 4,
                tooltipY + tooltipHeight + 3,
                zLevel,
                backgroundColor,
                backgroundColor);
        fillGradient(
                matrix4f,
                bufferBuilder,
                tooltipX - 3,
                tooltipY - 3 + 1,
                tooltipX - 3 + 1,
                tooltipY + tooltipHeight + 3 - 1,
                zLevel,
                borderColorStart,
                borderColorEnd);
        fillGradient(
                matrix4f,
                bufferBuilder,
                tooltipX + tooltipWidth + 2,
                tooltipY - 3 + 1,
                tooltipX + tooltipWidth + 3,
                tooltipY + tooltipHeight + 3 - 1,
                zLevel,
                borderColorStart,
                borderColorEnd);
        fillGradient(
                matrix4f,
                bufferBuilder,
                tooltipX - 3,
                tooltipY - 3,
                tooltipX + tooltipWidth + 3,
                tooltipY - 3 + 1,
                zLevel,
                borderColorStart,
                borderColorStart);
        fillGradient(
                matrix4f,
                bufferBuilder,
                tooltipX - 3,
                tooltipY + tooltipHeight + 2,
                tooltipX + tooltipWidth + 3,
                tooltipY + tooltipHeight + 3,
                zLevel,
                borderColorEnd,
                borderColorEnd);
        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        bufferBuilder.end();
        BufferUploader.end(bufferBuilder);
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();

        // text
        MultiBufferSource.BufferSource bufferSource =
                MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        poseStack.translate(0.0, 0.0, 400.0);
        int s = tooltipY;
        boolean first = true;
        for (ClientTooltipComponent line : lines) {
            line.renderText(font, tooltipX, s, matrix4f, bufferSource);
            s += line.getHeight() + (first ? 2 : 0);
            first = false;
        }
        bufferSource.endBatch();
        poseStack.popPose();
    }

    public static void copyImageToClipboard(BufferedImage bi) {
        class ClipboardImage implements Transferable {
            Image image;

            public ClipboardImage(Image image) {
                this.image = image;
            }

            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] {DataFlavor.imageFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                if (!DataFlavor.imageFlavor.equals(flavor)) throw new UnsupportedFlavorException(flavor);
                return this.image;
            }
        }

        ClipboardImage ci = new ClipboardImage(bi);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ci, null);
    }

    public static BufferedImage createScreenshot(RenderTarget fb) {
        NativeImage image = new NativeImage(fb.width, fb.height, false);
        RenderSystem.bindTexture(fb.getColorTextureId());
        image.downloadTexture(0, false);
        image.flipY();

        int[] pixelValues = image.makePixelArray();
        BufferedImage bufferedimage = new BufferedImage(fb.width, fb.height, BufferedImage.TYPE_INT_ARGB);
        bufferedimage.setRGB(0, 0, fb.width, fb.height, pixelValues, 0, fb.width);
        return bufferedimage;
    }
}
