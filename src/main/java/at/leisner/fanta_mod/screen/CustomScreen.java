package at.leisner.fanta_mod.screen;

import at.leisner.fanta_mod.FantaMod;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.List;

import static java.awt.SystemColor.text;

public class CustomScreen extends Screen {
    private JsonObject j = null;
    public boolean renderBackground = false;
    protected CustomScreen(Text title, JsonObject jsonObject, boolean renderBackground) {
        super(title);
        j = jsonObject;
        this.renderBackground = renderBackground;
    }

    @Override
    protected void init() {
        super.init();
        createWidget();
    }

    private void createWidget() {
        List<JsonElement> widgets = j.get("widget").getAsJsonArray().asList();
        int num = 0;
        for (JsonElement widgetTemp : widgets) {
            JsonObject widget = widgetTemp.getAsJsonObject();
            if (!widget.has("type")) continue;
            String type = widget.get("type").getAsString();
            int x = widget.has("x") ? widget.get("x").getAsInt() : 10;
            int y = widget.has("y") ? widget.get("y").getAsInt() : 10;
            int width = widget.has("width") ? widget.get("width").getAsInt() : 100;
            int height = widget.has("height") ? widget.get("height").getAsInt() : 20;
            int id = widget.has("id") ? widget.get("id").getAsInt() : num;
            boolean closeOnClick = widget.has("closeOnClick") && widget.get("closeOnClick").getAsBoolean();
            boolean canClick = !widget.has("canClick") || widget.get("canClick").getAsBoolean();
            Tooltip tooltip = Tooltip.of(Text.of(widget.has("tooltip") ? widget.get("tooltip").getAsString() : ""));

            if (type.equals("button")) {
                String text = widget.has("text") ? widget.get("text").getAsString() : "Button-"+num;
                int finalNum = num;
                ButtonWidget buttonWidget = ButtonWidget.builder(Text.of(text),(button) -> {
                    if (closeOnClick) close();
                    ByteBuf byteBuf = Unpooled.buffer();
                    PacketByteBuf packetByteBuf = new PacketByteBuf(byteBuf);
                    packetByteBuf.writeString("{type:button,id:" + finalNum + "}");
                    FantaMod.sendPluginMessage(packetByteBuf);
                }).dimensions(x, y, width, height)
                        .tooltip(tooltip)
                        .build();
                if (canClick) this.addDrawableChild(buttonWidget);
                else this.addDrawable(buttonWidget);
            } else if (type.equals("textField")) {
                String text = widget.has("text") ? widget.get("text").getAsString() : "";
                TextFieldWidget textFieldWidget = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, x, y, width, height, Text.of(text));
                textFieldWidget.setPlaceholder(Text.of(widget.has("placeholder") ? widget.get("placeholder").getAsString() : ""));
                textFieldWidget.setSuggestion(widget.has("suggestion") ? widget.get("suggestion").getAsString() : "");
                textFieldWidget.setEditable(!widget.has("editable") || widget.get("editable").getAsBoolean());
                textFieldWidget.setTooltip(tooltip);
                if (canClick) this.addDrawableChild(textFieldWidget);
                else this.addDrawable(textFieldWidget);
            } else if (type.equals("checkbox")) {
                Text text = Text.of(widget.has("text") ? widget.get("text").getAsString() : "");
                int finalNum = num;
                CheckboxWidget checkboxWidget = CheckboxWidget.builder(text,MinecraftClient.getInstance().textRenderer)
                        .tooltip(tooltip)
                        .checked(widget.has("checked") && widget.get("checked").getAsBoolean())
                        .pos(x,y)
                        .callback((checkbox,checked) -> {
                            ByteBuf byteBuf = Unpooled.buffer();
                            // Erstellen eines PacketByteBuf mit dem ByteBuf
                            PacketByteBuf packetByteBuf = new PacketByteBuf(byteBuf);

                            // Hinzufügen von Daten zum PacketByteBuf
                            // Beispiel: Ein String, der als UTF-8 kodiert wird
                            packetByteBuf.writeString("screen/custom<\n>");
                            packetByteBuf.writeString("{type:checkbox,id:"+ finalNum +",checked:"+checked+"}");
                            FantaMod.sendPluginMessage(packetByteBuf);
                        })
                        .build();
                if (canClick) this.addDrawableChild(checkboxWidget);
                else this.addDrawable(checkboxWidget);
            } else if (type.equals("slider")) {
                Text text = Text.of(widget.has("text") ? widget.get("text").getAsString() : "");

                // Beispielhafte Implementierung eines SliderWidgets basierend auf angenommenen Parametern
                double minValue = widget.has("minValue") ? widget.get("minValue").getAsDouble() : 0.0;
                double maxValue = widget.has("maxValue") ? widget.get("maxValue").getAsDouble() : 100.0;
                double defaultValue = widget.has("defaultValue") ? widget.get("defaultValue").getAsDouble() : 50.0;
                SliderWidget sliderWidget = new SliderWidget(x, y, width, height, text, defaultValue) {
                    @Override
                    protected void updateMessage() {
                    }
                    @Override
                    protected void applyValue() {

                    }
                };
                sliderWidget.setTooltip(tooltip);
                this.addDrawableChild(sliderWidget);
            } else if (type.equals("toggleButton")) {
                Text text = Text.of(widget.has("text") ? widget.get("text").getAsString() : "");
                // Beispielhafte Implementierung eines ToggleButtonWidgets
                boolean defaultToggled = widget.has("toggled") && widget.get("toggled").getAsBoolean();
                ToggleButtonWidget toggleButtonWidget = new ToggleButtonWidget(x, y, width, height, defaultToggled);
                toggleButtonWidget.setTooltip(tooltip);
                this.addDrawableChild(toggleButtonWidget);
            }

            num++;
        }
    }
    @Override
    public void render(DrawContext drawCentered, int mouseX, int mouseY, float delta) {
        if (renderBackground)
            this.renderBackground(drawCentered, mouseX, mouseY, delta); // Zeichnet den Standardhintergrund
        super.render(drawCentered, mouseX, mouseY, delta);

        // Titel rendern
        drawCentered.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return super.shouldCloseOnEsc();
    }

    @Override
    public void close() {
        super.close();
    }

}
