package at.leisner.fanta_mod.screen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ScreenManager {
    public void parseMSG(List<String> args) {
        String cmd = args.get(0);
        try {
            JsonObject jsonObject = JsonParser.parseString(args.get(1)).getAsJsonObject();
            boolean renderBackground = jsonObject.has("renderBackground") && jsonObject.get("renderBackground").getAsBoolean();
            if (cmd.equals("optional") && jsonObject.has("buttons")) {
                String title = jsonObject.has("title") ? jsonObject.get("title").getAsString() : "None";
                boolean canCloseWithEsc = !jsonObject.has("canCloseWithEsc") || jsonObject.get("canCloseWithEsc").getAsBoolean();
                List<String> buttons = new ArrayList<>();
                for (JsonElement jsonElement : jsonObject.get("buttons").getAsJsonArray().asList()) {
                    buttons.add(jsonElement.getAsString());
                }
                MinecraftClient.getInstance().execute(() -> {
                    MinecraftClient.getInstance().setScreen(new OptionalScreen(Text.of(title),buttons,canCloseWithEsc, renderBackground));
                });
            } else if (cmd.equals("custom")) {
                String title = jsonObject.has("title") ? jsonObject.get("title").getAsString() : "None";
                MinecraftClient.getInstance().execute(() -> {
                    MinecraftClient.getInstance().setScreen(new CustomScreen(Text.of(title),jsonObject, renderBackground));
                });
            } else if (cmd.equals("builder")) {
                String title = jsonObject.has("title") ? jsonObject.get("title").getAsString() : "";
                MinecraftClient.getInstance().execute(() -> {
                    MinecraftClient.getInstance().setScreen(new ScreenBuilder(Text.of(title)));
                });
            }
        } catch (JsonSyntaxException e) {
//            throw new RuntimeException(e);
        }
    }
}
