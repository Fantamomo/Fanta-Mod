package at.leisner.fanta_mod.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScreenBuilder extends Screen {
    private JsonArray widgetList;
    private Map<String, ButtonWidget> buttons;
    private Map<String, TextFieldWidget> textFields;
    private boolean showDropdown = false;
    private boolean showWidgetTypeDropdown = false;
    private int dropdownX, dropdownY;
    private ButtonWidget addButton;
    private ButtonWidget removeButton;

    protected ScreenBuilder(Text title) {
        super(title);
        this.widgetList = new JsonArray();
        this.buttons = new HashMap<>();
        this.textFields = new HashMap<>();
    }

    private void toggleDropdownMenu(boolean show, int x, int y) {
        if (show) {
            addButton = addDrawableChild(ButtonWidget.builder(Text.of("Add"), button -> {
                showWidgetTypeDropdown = true; // Öffnet das sekundäre Dropdown-Menü für die Auswahl des Widget-Typs
                showDropdown = false; // Schließt das Haupt-Dropdown-Menü
            }).position(x, y).size(100, 20).build());

            // Der "Remove"-Button wird jetzt dynamisch hinzugefügt, wenn auf ein Widget geklickt wird
        } else {
            remove(addButton);
            // Remove-Button entfernen, falls vorhanden
            if (removeButton != null) {
                remove(removeButton);
                removeButton = null;
            }
        }
        showDropdown = show;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Vorherige Implementierung...
        if (showWidgetTypeDropdown) {
            // Annahme: Widget-Typen Dropdown ist 100x50px mit zwei Optionen
            if (mouseX >= dropdownX && mouseX <= dropdownX + 100 && mouseY >= dropdownY && mouseY <= dropdownY + 50) {
                if (mouseY < dropdownY + 25) {
                    // "Button" ausgewählt
                    addWidget(createWidgetConfig("button"), mouseX, mouseY);
                } else {
                    // "TextField" ausgewählt
                    addWidget(createWidgetConfig("textField"), mouseX, mouseY);
                }
                showWidgetTypeDropdown = false;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);
        if (showWidgetTypeDropdown) {
            renderWidgetTypeDropdown(drawContext, dropdownX, dropdownY);
        }
    }

    private void renderWidgetTypeDropdown(DrawContext drawContext, int x, int y) {
        drawContext.fill(x, y, x + 100, y + 50, 0xFFAAAAAA); // Graues Rechteck für Dropdown
        drawContext.drawText(textRenderer, "Button", x + 5, y + 5, 0xFFFFFFFF,true);
        drawContext.drawText(textRenderer, "TextField", x + 5, y + 30, 0xFFFFFFFF,true);
    }

    private JsonObject createWidgetConfig(String type) {
        JsonObject config = new JsonObject();
        config.addProperty("type", type);
        // Weitere Konfigurationsdetails je nach Widget-Typ
        return config;
    }

    private void addWidget(JsonObject widgetConfig, double x, double y) {
        // Implementierung zum Hinzufügen eines Widgets basierend auf der widgetConfig
        // und den Koordinaten x, y
        String type = widgetConfig.get("type").getAsString();
        String id = UUID.randomUUID().toString();
        switch (type) {
            case "button":
                ButtonWidget newButton = ButtonWidget.builder(Text.of("New Button"), button -> {})
                        .dimensions((int)x, (int)y, 100, 20)
                        .build();
                buttons.put(id, newButton);
                addDrawableChild(newButton);
                break;
            case "textField":
                TextFieldWidget newTextField = new TextFieldWidget(textRenderer, (int)x, (int)y, 100, 20, Text.of(""));
                textFields.put(id, newTextField);
                addDrawableChild(newTextField);
                break;
        }
        widgetList.add(widgetConfig);
    }

    private void removeWidget(String id, String type) {
        switch (type) {
            case "button":
                buttons.remove(id);
                break;
            case "textField":
                textFields.remove(id);
                break;
        }
        // Hier könnte man auch das Widget aus widgetList entfernen, wenn man die IDs dort ebenfalls speichert
    }

    @Override
    protected void init() {
        super.init();
        // Hier könnte man Initialisierungslogik hinzufügen
    }

    private void saveConfiguration() {
        // Implementierung zum Speichern der Konfiguration
    }
}
