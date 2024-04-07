package at.leisner.fanta_mod.screen;

import at.leisner.fanta_mod.FantaMod;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.List;

public class OptionalScreen extends Screen {
    private final List<String> buttonLabels;
    public boolean renderBackground = false;
    private boolean onClick = false;
    private boolean canCloseWithEsc = true;

    // Konstruktor
    public OptionalScreen(Text title, List<String> buttonLabels, boolean canCloseWithEsc,boolean renderBackground) {
        super(title);
        this.buttonLabels = buttonLabels;
        this.canCloseWithEsc = canCloseWithEsc;
        this.renderBackground = renderBackground;
    }

    @Override
    protected void init() {
        super.init();

        // Text in der Mitte der Oberseite
        int titleWidth = this.textRenderer.getWidth(title);
        int titleX = (this.width - titleWidth) / 2;
        int titleY = 15;

        // Buttons unter dem Titel dynamisch hinzufügen
        int startY = this.height / 2 - (buttonLabels.size() * 20) / 2;
        for (int i = 0; i < buttonLabels.size(); i++) {
            int buttonY = startY + (i * 24); // 20 für die Buttonhöhe, 4 für den Abstand
            int finalI = i;
            this.addDrawableChild(ButtonWidget.builder(Text.of(buttonLabels.get(i)), button -> {
                onClick = true;
                close();
                PacketByteBuf packetByteBuf = PacketByteBufs.create();

                // Hinzufügen von Daten zum PacketByteBuf
                // Beispiel: Ein String, der als UTF-8 kodiert wird
                packetByteBuf.writeString("screen/optional");
                packetByteBuf.writeString("{button:{name:\"" + buttonLabels.get(finalI) + "\",id:" + finalI + "}}");
                FantaMod.sendPluginMessage(packetByteBuf);
            }).dimensions(this.width / 2 - 100, buttonY, 200, 20).build());
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
        return canCloseWithEsc;
    }

    @Override
    public void close() {
        super.close();
        if (onClick) return;
        ByteBuf byteBuf = Unpooled.buffer();
        // Erstellen eines PacketByteBuf mit dem ByteBuf
        PacketByteBuf packetByteBuf = new PacketByteBuf(byteBuf);

        // Hinzufügen von Daten zum PacketByteBuf
        // Beispiel: Ein String, der als UTF-8 kodiert wird
        packetByteBuf.writeString("screen/optional<\n>");
        packetByteBuf.writeString("{hasClosedWithEsc:true}");
        FantaMod.sendPluginMessage(packetByteBuf);
    }
}
