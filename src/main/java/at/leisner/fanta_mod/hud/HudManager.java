package at.leisner.fanta_mod.hud;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.registry.Registries;
public class HudManager {
    private HashMap<Integer, HudPlacer> hudHashMap = new HashMap<>();

    public HashMap<Integer, HudPlacer> getHudHashMap() {
        return hudHashMap;
    }

    public void add(HudPlacer hudPlacer, int id) {
        hudHashMap.put(id,hudPlacer);
    }
    public void parseMSG(List<String> args) {
        String cmd = args.get(0);
        if (args.size() < 2) return;
        if (cmd.equals("clear")) {
            hudHashMap.clear();
            return;
        }
        try {
            JsonObject jsonObject = JsonParser.parseString(args.get(1)).getAsJsonObject();
            int x = jsonObject.has("x") ? jsonObject.get("x").getAsInt() : 10;
            int y = jsonObject.has("y") ? jsonObject.get("y").getAsInt() : 10;
            int width = jsonObject.has("width") ? jsonObject.get("width").getAsInt() : 10;
            int height = jsonObject.has("height") ? jsonObject.get("height").getAsInt() : 10;
            int id = jsonObject.has("id") ? jsonObject.get("id").getAsInt() : hudHashMap.keySet().size();
            switch (cmd) {
                case "item" -> {
                    Identifier identifier = jsonObject.has("item") ? Identifier.tryParse(jsonObject.get("item").getAsString()) : null;
                    if (identifier == null || identifier.getPath().equals("air")) {
                        hudHashMap.remove(id);
                        return;
                    }
                    ItemStack itemStack = new ItemStack(Registries.ITEM.get(identifier), 1);
                    HudItem item = new HudItem(itemStack, x, y);
                    add(item, id);
                }
                case "text" -> {
                    Text text = Text.of(jsonObject.has("text") ? jsonObject.get("text").getAsString() : "");
                    if (text == null || text.equals(Text.of(""))) {
                        hudHashMap.remove(id);
                        return;
                    }
                    HudText hudText = new HudText(text, x, y);
                    hudText.color = jsonObject.has("color") ? jsonObject.get("color").getAsInt() : 0xFFFFFF;
                    hudText.shadow = jsonObject.has("shadow") && jsonObject.get("shadow").getAsBoolean();
                    add(hudText, id);
                }
                case "border" -> {
                    int color = jsonObject.has("color") ? jsonObject.get("color").getAsInt() : 0xFFFFFF;
                    if (height == 0 && width == 0) {
                        hudHashMap.remove(id);
                        return;
                    }
                    HudBorder hudBorder = new HudBorder(x, y, width, height, color);
                    add(hudBorder, id);
                }
                case "fill" -> {
                    int color = jsonObject.has("color") ? jsonObject.get("color").getAsInt() : 0xFFFFFF;
                    if (height == 0 && width == 0) {
                        hudHashMap.remove(id);
                        return;
                    }
                    HudFill hudFill = new HudFill(x, y, width, height, color);
                    add(hudFill, id);
                }
                case "tooltip" -> {
                    List<Text> textList = new ArrayList<>();
                    for (JsonElement jsonElement : jsonObject.get("text").getAsJsonArray().asList())
                        textList.add(Text.of(jsonElement.getAsString()));
                    if (textList.isEmpty()) {
                        hudHashMap.remove(id);
                        return;
                    }
                    HudTooltip hudTooltip = new HudTooltip();
                    hudTooltip.X = x;
                    hudTooltip.Y = y;
                    hudTooltip.textList = textList;
                    add(hudTooltip, id);
                }
                case "texture" -> {
                    HudTexture hudTooltip = new HudTexture();
                    hudTooltip.X = x;
                    hudTooltip.Y = y;
                    hudTooltip.width = width;
                    hudTooltip.height = height;
                    hudTooltip.U = jsonObject.has("u") ? jsonObject.get("u").getAsInt() : 10;
                    hudTooltip.V = jsonObject.has("v") ? jsonObject.get("v").getAsInt() : 10;
                    add(hudTooltip, id);
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

    }
}
