package at.leisner.fanta_mod;

import at.leisner.fanta_mod.client.MessageParser;
import at.leisner.fanta_mod.hud.HudManager;
import at.leisner.fanta_mod.hud.HudRendere;
import at.leisner.fanta_mod.screen.ScreenManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FantaMod implements ModInitializer {
    static final Identifier MESSAGE_IDENTIFIER = new Identifier("fanta", "mod");
    private static FantaMod mod;
    private HudManager hudManager;
    private HudRendere hudRendere;
    private ScreenManager screenManager;

    @Override
    public void onInitialize() {
        mod = this;
        hudManager = new HudManager();
        registerReceivers();
        hudRendere = new HudRendere();
        screenManager = new ScreenManager();

    }

    @Environment(value = EnvType.CLIENT)
    public void registerReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(MESSAGE_IDENTIFIER,
                (client, handler, buf, responseSender) -> {
                    List<String> read = new ArrayList<>();
                    while (true) {
                        try {
                            buf.readString();
                            read.add(buf.readString());
                        } catch (IndexOutOfBoundsException e) {
                            break;
                        }
                    }
                    MessageParser.parseMessage(read);
                    System.out.println(read.stream().map(String::valueOf).collect(Collectors.joining(", ", "", "")));
                }
        );
    }

    public static void sendPluginMessage(PacketByteBuf date) {
        // Senden der Plugin-Nachricht
        ClientPlayNetworking.send(MESSAGE_IDENTIFIER, date);
    }

    public static FantaMod getInstance() {
        return mod;
    }

    public HudManager getHudManager() {
        return hudManager;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }
}