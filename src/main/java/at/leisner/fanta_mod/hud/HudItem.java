package at.leisner.fanta_mod.hud;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HudItem extends HudPlacer {
    public float delta;
    public ItemStack item = ItemStack.EMPTY;
    public int X = 10;
    public int Y = 10;
    public int seed = 0;
    public int Z = 0;


    public HudItem(ItemStack item) {
        this.item = item;
    }

    public HudItem(ItemStack item, int posX, int posY) {
        this.item = item;
        this.X = posX;
        this.Y = posY;
    }
}
