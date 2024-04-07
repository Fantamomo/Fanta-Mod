package at.leisner.fanta_mod.client;

import at.leisner.fanta_mod.FantaMod;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

import java.util.List;

public class MessageParser {
    public static void parseMessage(List<String> args) {
        if (args.isEmpty()) return;
        String cmd = args.get(0);
        if (cmd.startsWith("hud/")) {
            args.set(0, cmd.replace("hud/",""));
            FantaMod.getInstance().getHudManager().parseMSG(args);
            return;
        }
        if (cmd.startsWith("screen/")) {
            args.set(0, cmd.replace("screen/",""));
            FantaMod.getInstance().getScreenManager().parseMSG(args);
            return;
        }
        if (cmd.startsWith("info/")) {
            if (cmd.equals("info/join")) {
                ByteBuf byteBuf = Unpooled.buffer();
                PacketByteBuf packetByteBuf = new PacketByteBuf(byteBuf);
                packetByteBuf.writeString("{type:\"info/join\"}");
                FantaMod.sendPluginMessage(packetByteBuf);
                return;
            }
        }
    }
}
