package at.leisner.fanta_mod.hud;

import at.leisner.fanta_mod.FantaMod;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class HudRendere implements HudRenderCallback {
    public HudRendere() {
        HudRenderCallback.EVENT.register(this);
    }

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        FantaMod mod = FantaMod.getInstance();
        HudManager hudManager = mod.getHudManager();
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        for (HudPlacer hudPlacer : hudManager.getHudHashMap().values()) {
            if (hudPlacer instanceof HudItem item) {
                drawContext.drawItem(item.item, item.X, item.Y, item.seed, item.Z);
            } else if (hudPlacer instanceof HudText text) {
                drawContext.drawText(textRenderer, text.text, text.X, text.Y, text.color, text.shadow);
            } else if (hudPlacer instanceof HudBorder border) {
                drawContext.drawBorder(border.X, border.Y, border.width, border.height, border.color);
            } else if (hudPlacer instanceof HudFill fill) {
                drawContext.fill(fill.X, fill.Y, fill.width, fill.height, fill.color);
            } else if (hudPlacer instanceof HudTooltip tooltip) {
                drawContext.drawTooltip(textRenderer, tooltip.textList, tooltip.X, tooltip.Y);
            } else if (hudPlacer instanceof HudItemTooltip tooltip) {
                drawContext.drawItemTooltip(textRenderer, tooltip.item, tooltip.X, tooltip.Y);
            } else if (hudPlacer instanceof HudTexture texture) {
                drawContext.drawTexture(texture.texture,texture.X, texture.Y,texture.U, texture.V, texture.width, texture.height);
            }

        }
    }
}
