package at.leisner.fanta_mod.hud;

import org.jetbrains.annotations.NotNull;

public class HudFill extends HudPlacer {
    public int X = 10;
    public int Y = 10;
    public int width = 10;
    public int height = 10;
    public int color = 0xFFFFFF;

    public HudFill(int x, int y, int width, int height, int color) {
        X = x;
        Y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }
}
