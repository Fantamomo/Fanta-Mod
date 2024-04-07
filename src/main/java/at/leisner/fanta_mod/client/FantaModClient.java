package at.leisner.fanta_mod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class FantaModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("KeyMod Client is initializing.");

        // Initialisieren Sie hier Ihre client-seitigen Funktionalitäten.
    }
}