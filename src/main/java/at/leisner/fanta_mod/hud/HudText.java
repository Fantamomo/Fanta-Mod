package at.leisner.fanta_mod.hud;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class HudText extends HudPlacer {
    public Text text = Text.of("");
    public int X = 10;
    public int Y = 10;
    public int color = 0xFFFFFF;
    public boolean shadow = true;

    public HudText(Text text) {
        this.text = text;
    }

    public HudText(Text text, int posX, int posY) {
        this.text = text;
        this.X = posX;
        this.Y = posY;
    }
}
